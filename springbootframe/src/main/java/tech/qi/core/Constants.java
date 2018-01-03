package tech.qi.core;

/**
 *
 * @author wangqi
 * @date 2017/12/27 上午11:01
 */
public class Constants {
    public final static String SPRING_SECURITY_ROLE_PREFIX = "ROLE_";
    public final static String SPRING_SECURITY_USER_ROLE = "USER";
    public final static String SPRING_SECURITY_BUYER_ROLE = "BUYER";
    public final static String REDIS_SESSION_NAME = "Distributed-Session:";

    public final static int DEFAULT_EXCEPTION = -1;

    public final static String REST_STATUS_KEY = "status";
    public final static String REST_MESSAGE_KEY = "message";
    public final static String REST_DATA_KEY = "data";


    public final static String HASHID_SALT = "WsdYo";
    public final static int HASHID_MIN_LENGTH = 4;
    public final static String HASHID_ALPHABET = "abcdefghijklmnpqrstuvwxyz";
}