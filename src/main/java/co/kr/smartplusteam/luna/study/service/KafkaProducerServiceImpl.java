package co.kr.smartplusteam.luna.study.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendTopic(String topic, byte[] data) {
        kafkaTemplate.send(topic, data);
    }

    @Override
    public void sendTopic(String topic, Object data) {
        kafkaTemplate.send(topic, data);
    }

    @Override
    public void sendMultiTopic(String topicArr, Object data) {
//        for (String topic : topicArr.split("|")) {
//            sendTopic(topic, data);
//        }
    }


}
