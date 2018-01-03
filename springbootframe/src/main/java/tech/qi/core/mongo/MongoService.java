package tech.qi.core.mongo;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import static com.mongodb.client.model.Filters.*;

/**
 *
 * @author wangqi
 * @date 2017/11/2 下午1:50
 *
 */
@Service
@ConditionalOnBean(MongoConfig.class)
public class MongoService {
    protected static final Logger logger = LoggerFactory.getLogger(MongoService.class);
    private static final String UID = "uuid";

    @Autowired
    MongoConfig mongoConfig;

    public boolean save(JSONObject info, String database, String collection) {
        Assert.isTrue( info.keySet().contains(UID), "必须有一个唯一标识字段：" + UID );
        Document document = new Document();
        for( String key : info.keySet() ){
            document.append(key, info.get(key));
        }
        try {
            MongoDatabase db = mongoConfig.client().getDatabase(database);
            MongoCollection<Document> coll = db.getCollection(collection);
            coll.insertOne( document );
        } catch (Exception e) {
            logger.error(" Insert into mongo error. db:" + database + " collection:" + collection, e);
            return false;
        }
        return true;
    }

    /**
     * Document 必须有一个 uuid 字段。只取第一个。
     * @param uuid
     * @param database
     * @param collection
     * @return
     */
    public JSONObject getById(String uuid, String database, String collection) {
        MongoDatabase db = mongoConfig.client().getDatabase(database);
        MongoCollection<Document> coll = db.getCollection(collection);

        FindIterable<Document> resList = coll.find( eq(UID, uuid) );
        Document found = resList.first();
        JSONObject result = new JSONObject();

        found.keySet().forEach( key -> result.put(key, found.get(key)) );
        return result;
    }

    public long deleteById(String uuid, String database, String collection) {
        MongoDatabase db = mongoConfig.client().getDatabase(database);
        MongoCollection<Document> coll = db.getCollection(collection);

        DeleteResult deleteResult = coll.deleteMany( eq(UID, uuid) );

        return deleteResult.getDeletedCount();
    }

    /**
     * 注意！！！
     * 这里没有做翻页，是根据业务场景来的。因为爬虫在同一个 SerialId 下是不会有非常多请求的，小于一百个，所以可以不做分页。
     *
     * 用法：Map<key, matchType ### val>。MatchType包括了 Wildcard|Regexp
     */
    public List<JSONObject> getListByFieldVal(Map<String, String> fieldVals, String database, String collection) {
        MongoDatabase db = mongoConfig.client().getDatabase(database);
        MongoCollection<Document> coll = db.getCollection(collection);
        String splitter = " ### ";

        Bson condition = null;
        Object[] keySet = fieldVals.keySet().toArray();
        for( int i=0; i<keySet.length; i++ ){
            String key = keySet[i].toString();
            String val = fieldVals.get(key).toString();
            if( val.contains(splitter) ){
                String[] valSplit = val.split(splitter);
                if(valSplit[0].equals("regexp")) {
                    condition = condition==null ? regex(key, valSplit[1]) : and(condition, regex(key, valSplit[1]));
                }else {
                    condition = condition==null ? eq(key, valSplit[1]) : and(condition, eq(key, valSplit[1]));
                }
            }else {
                condition = condition==null ? eq(key, val) : and(condition, eq(key, val));
            }
        }

        FindIterable<Document> resList = coll.find(condition);
        List<JSONObject> jsonRes = new ArrayList<>();
        resList.forEach((Consumer<Document>) document -> {
            JSONObject one = new JSONObject();
            document.keySet().forEach( key -> one.put(key, document.get(key)) );
            jsonRes.add(one);
        });
        return jsonRes;
    }
}
