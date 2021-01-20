package co.kr.smartplusteam.luna.study.vo;

public interface errorType {

    // request Div
    String INSERT_REQUEST = "I";
    String SELECT_REQUEST = "S";
    String COMPONENT_REQUEST = "C";

    // mongo err type
    String MONGO_ERR_TYPE_APICALL = "apicall-err";
    String MONGO_ERR_TYPE_ELASTIC = "elastic-err";
    String MONGO_ERR_TYPE_REDIS = "redis-err";
    String MONGO_ERR_TYPE_KAFKA = "kafka-err";
    String MONGO_ERR_TYPE_MONGO = "mongo-err";
    String MONGO_ERR_TYPE_MYSQL = "mysql-err";
    String MONGO_ERR_TYPE_ETC = "etc-err";
}