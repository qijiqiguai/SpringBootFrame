package tech.qi.core.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author wangqi
 * @date 2018/1/3 下午1:58
 */
@Configuration
@ConfigurationProperties(prefix="mongo")
@ConditionalOnProperty(name="mongo.enable", havingValue="true")
public class MongoConfig {
    protected static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    private static final String multiSplitter = ",";
    private static final String innerSplitter = ":";

    /**
     * Mongo 服务器可以加多个的，所以可能有多个连接信息，因此定义如下 ip:port,ip:port
     * 一个服务器和端口用分号隔开，两个组之间用逗号隔开，这样配置简洁一点
     */
    private String server;

    /**
     * Mongo 的认证方式是 user + database + pass, 所以可能有多个连接信息，因此定义如下 user:database:pass,user:database:pass
     * 一个认证方式的三个信息内部用分号隔开，两个认证方式之间用逗号隔开, 这样配置简洁一点
     */
    private String credential;

    private MongoClient client;

    @PostConstruct
    public void init(){
        Assert.isTrue( null!=server && !"".equals(server.trim()), "Empty Mongo Server Config" );
        client();
    }

    public synchronized MongoClient client(){
        if( null == client ) {
            List<ServerAddress> serverList = new ArrayList<>();
            if( server.contains(multiSplitter) ){
                String[] multiSer = server.split(multiSplitter);
                for( int i=0; i<multiSer.length; i++ ){
                    serverList.add( makeSA(multiSer[i]) );
                }
            }else {
                serverList.add( makeSA(server) );
            }

            List<MongoCredential> mongoCredentialList = new ArrayList<>();
            if (null!=credential && !credential.trim().equals("")) {
                if( credential.contains(multiSplitter) ){
                    String[] multiCre = credential.split(multiSplitter);
                    for( int i=0; i<multiCre.length; i++ ){
                        mongoCredentialList.add( makeMC(multiCre[i]) );
                    }
                }else {
                    mongoCredentialList.add( makeMC(credential) );
                }
            }
            client = new MongoClient(serverList, mongoCredentialList);
        }
        return client;
    }

    /**
     * 输入必须是 user,database,pass 的形式, Make MongoCredential
     * @param input
     * @return
     */
    private MongoCredential makeMC(String input) {
        Assert.isTrue(!input.contains(multiSplitter) && input.contains(innerSplitter),
                "Invalid Mongo Credential:" + input);
        String[] creDetails = input.split(innerSplitter);
        Assert.isTrue( 3==creDetails.length,  "Invalid Mongo Credential:" + input);
        MongoCredential credential = MongoCredential.createCredential(
                creDetails[0].trim(), creDetails[1].trim(), creDetails[2].trim().toCharArray());
        return credential;
    }

    /**
     * 输入必须是 server,port的形式, Make ServerAddress
     * @param input
     * @return
     */
    private ServerAddress makeSA(String input ){
        Assert.isTrue(!input.contains(multiSplitter) && input.contains(innerSplitter),
                "Invalid Mongo Server Info:" + input);
        String[] serverDetails = input.split(innerSplitter);
        Assert.isTrue( 2==serverDetails.length,  "Invalid Mongo Server Info:" + input);
        String server = serverDetails[0].trim();
        int port;
        try {
            port = Integer.parseInt(serverDetails[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Mongo Server Info:" + input);
        }
        Assert.isTrue( port > 0,  "Invalid Mongo Server Info:" + input);
        ServerAddress sa = new ServerAddress(server, port);
        return sa;
    }


    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}
