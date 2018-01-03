package tech.qi.core.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author wangqi
 * @date 2018/1/3 下午1:58
 */
@Configuration
@ConfigurationProperties(prefix="elasticsearch")
@ConditionalOnProperty(name="elasticsearch.enable", havingValue="true")
public class EsConfig {
    protected static final Logger logger = LoggerFactory.getLogger(EsService.class);

    private String clusterName;
    private String host1;
    private String host2;
    private String host3;
    private int port;

    private TransportClient client = null;


    @PostConstruct
    public void init(){
        client();
    }

    /**
     * 单例模式
     * @return
     */
    public synchronized TransportClient client(){
        if( null == client ){
            Settings settings = Settings.builder()
                    .put("cluster.name", clusterName)
                    .put("client.transport.sniff", true)
                    .put("client.transport.ignore_cluster_name", false)
                    .put("client.transport.ping_timeout", 50, TimeUnit.SECONDS)
                    .build();
            try {
                if(host2==null || host2.trim().equals("")){
                    client = new PreBuiltTransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host1), port));
                }else {
                    client = new PreBuiltTransportClient(settings)
                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host1), port))
                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host2), port))
                            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host3), port));
                }
            } catch (UnknownHostException e) {
                logger.error("找不到ES主机", e);
            }
        }
        return client;
    }


    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHost1() {
        return host1;
    }

    public void setHost1(String host1) {
        this.host1 = host1;
    }

    public String getHost2() {
        return host2;
    }

    public void setHost2(String host2) {
        this.host2 = host2;
    }

    public String getHost3() {
        return host3;
    }

    public void setHost3(String host3) {
        this.host3 = host3;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TransportClient getClient() {
        return client;
    }

    public void setClient(TransportClient client) {
        this.client = client;
    }
}
