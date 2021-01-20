package co.kr.smartplusteam.luna.study.service;

public interface KafkaProducerService {

    /**
     * 카프카 토픽에 데이터를 전송
     *
     * @param topic : kafka topic
     * @param data  : 전송할 데이터
     */
    void sendTopic(String topic, byte[] data);

    void sendTopic(String topic, Object data);

    /**
     * 다중 토픽에 동일한 데이터를 전송
     *
     * @param topicArr : kafka topic list (구분자 : | 파이프라인) (ex. abc|def|hig)
     * @param data     : 전송할 데이터
     */
    void sendMultiTopic(String topicArr, Object data);

}
