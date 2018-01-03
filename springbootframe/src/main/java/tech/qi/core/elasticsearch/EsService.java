package tech.qi.core.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 *
 * @author wangqi
 * @date 2017/9/11 下午5:00
 */
@Service
@ConditionalOnBean(EsConfig.class)
public class EsService {
    protected static final Logger logger = LoggerFactory.getLogger(EsService.class);

    @Autowired
    EsConfig esConfig;


    public EsService(){ }

    public boolean save(JSONObject info, String index, String type) {
        boolean status = false;
        for(int i=0; i<3; i++){
            try {
                IndexResponse res = esConfig.client().prepareIndex(index, type)
                        .setSource(new ObjectMapper().writeValueAsBytes(info), XContentType.JSON).get();
                status = true;
                break;
            } catch (NoNodeAvailableException cont) { // 在配置好能正常启动之后，如果还出现这个异常，证明是网络有问题，稍等再试
                logger.warn("找不到ES主机, 重试 " + i + "次", cont);
                continue;
            } catch (JsonProcessingException e) {
                logger.error("存储的字符串异常，不能转化成JSON", e);
                break;
            } catch (Exception e) {
                logger.error("未预料的异常", e);
                break;
            }
        }
        return status;
    }

    /**
     * 必须有一个 id 字段
      */
    public JSONObject getById(String id, String index, String type) {
        List<JSONObject> res = new ArrayList<>();
        SearchResponse response;
        ObjectMapper mapper = new ObjectMapper();

        response = esConfig.client().prepareSearch(index).setTypes( type )
                .setQuery(QueryBuilders.matchQuery("id", id))
                .execute()
                .actionGet();
        SearchHits hits = response.getHits();
        hits.iterator().forEachRemaining(
            o -> {
                try {
                    res.add(mapper.readValue(o.getSourceRef().utf8ToString(), JSONObject.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        );
        return res.size() > 0 ? res.get(0) : null;
    }

    public long deleteById(String id, String index) {
        BulkByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(esConfig.client())
                        .filter(QueryBuilders.matchQuery("id", id))
                        .source(index)
                        .execute()
                        .actionGet();
        long deleted = response.getDeleted();
        return deleted;
    }

    /**
     * 注意！！！
     * 这里没有做翻页，是根据业务场景来的。因为爬虫在同一个 SerialId 下是不会有非常多请求的，小于一百个，所以可以不做分页。
     *
     * 用法：Map<key, matchType ### val>。MatchType包括了 Wildcard|Regexp
     */
    public List<JSONObject> getListByFieldVal(Map<String, String> fieldVals, String index, String type) {
        String spliter = " ### ";

        int scrollSize = 1000;
        List<JSONObject> res = new ArrayList<>();
        SearchResponse response = null;
        int i = 0;
        ObjectMapper mapper = new ObjectMapper();

        while( response == null || response.getHits().hits().length != 0){
            SearchRequestBuilder req =  esConfig.client().prepareSearch( index ).setTypes( type );
            BoolQueryBuilder boolQuery = new BoolQueryBuilder();
            fieldVals.keySet().forEach(key -> {
                if( fieldVals.get(key).toString().contains(spliter) ){
                    String[] valSplit = fieldVals.get(key).toString().split(spliter);
                    if( valSplit[0].equals("wildcard") ){
                        boolQuery.must(QueryBuilders.wildcardQuery(key, valSplit[1]));
                    }else if(valSplit[0].equals("regexp")) {
                        boolQuery.must(QueryBuilders.regexpQuery(key, valSplit[1]));
                    }else if(valSplit[0].equals("term")) {
                        boolQuery.must(QueryBuilders.termQuery(key, valSplit[1]));
                    }else if(valSplit[0].equals("match")) {
                        boolQuery.must(QueryBuilders.matchQuery(key, valSplit[1]));
                    }
                }else {
                    boolQuery.must(QueryBuilders.wildcardQuery(key, fieldVals.get(key).toString()));
                }
            });
            response = req.setQuery(boolQuery)
                    .setSize(scrollSize)
                    .setFrom(i * scrollSize)
                    .execute()
                    .actionGet();
            SearchHits hits = response.getHits();
            hits.iterator().forEachRemaining(
                o -> {
                    try {
                        res.add(mapper.readValue(o.getSourceRef().utf8ToString(), JSONObject.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            );
            i++;
        }
        return res;
    }

}
