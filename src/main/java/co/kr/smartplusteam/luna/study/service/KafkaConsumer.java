package co.kr.smartplusteam.luna.study.service;

import co.kr.smartplusteam.luna.study.Repository.BusArrivalInfoRepository;
import co.kr.smartplusteam.luna.study.Repository.BusStationInfoRepository;
import co.kr.smartplusteam.luna.study.vo.BusArrivalInfo;
import co.kr.smartplusteam.luna.study.vo.BusStationInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class KafkaConsumer {

    @Autowired
    BusStationInfoRepository busStationInfoRepository;

    @Autowired
    BusArrivalInfoRepository busArrivalInfoRepository;

    @KafkaListener(topics = {"judy"})
    public void judyTopic(ConsumerRecord<String, String> consumerRecord) throws ParseException {
        log.info("kafka client(judy) start [{}]", consumerRecord);
        int partition = consumerRecord.partition();
        long topicOffset = consumerRecord.offset();
        String topicName = consumerRecord.topic();
        String topicMessage = consumerRecord.value();

        //String to Json
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(topicMessage);
        JSONObject jsonObj = (JSONObject) obj;

        //정류소 정보 MYSQL에 저장
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmssSSS", java.util.Locale.KOREA);
        String regiStationKey = formatter.format(new Date()) + "STATION" + ((JSONObject) jsonObj.get("parameter")).get("stationId").toString();
        String stationId = ((JSONObject) jsonObj.get("parameter")).get("stationId").toString();
        BusStationInfo busStationInfo = new BusStationInfo();
        busStationInfo.setREGISTRATION_ID(regiStationKey);
        busStationInfo.setSTATION_ID(stationId);
        busStationInfo.setCOUNT(String.valueOf(jsonObj.get("count")));

        BusStationInfo busStationInfo1 = busStationInfoRepository.save(busStationInfo);

        //도착정보 MYSQL에 저장
        //String to Json
        JSONParser parserResult = new JSONParser();
        Object resultobj = parserResult.parse(jsonObj.get("results").toString());
        JSONArray resultJsonArray = (JSONArray) resultobj;

        for(int i=0; i<resultJsonArray.size(); i++){
            JSONObject resultJsonObj = (JSONObject) resultJsonArray.get(i);
            String regiArrivalKey = formatter.format(new Date()) + "ARRIVAL" + String.valueOf(resultJsonObj.get("routeId"));

            BusArrivalInfo busArrivalInfo = new BusArrivalInfo();
            busArrivalInfo.setREGISTRATION_ID(regiArrivalKey);
            busArrivalInfo.setPARENT_REGISTRATION_ID(regiStationKey);
            busArrivalInfo.setSTATION_ID(stationId);
            busArrivalInfo.setROUTEID(String.valueOf(resultJsonObj.get("routeId")));
            busArrivalInfo.setROUTENUM(String.valueOf(resultJsonObj.get("routeNum")));
            busArrivalInfo.setROUTENM(String.valueOf(resultJsonObj.get("routeNm")));
            busArrivalInfo.setVIA(String.valueOf(resultJsonObj.get("via")));
            busArrivalInfo.setSTATIONORD(String.valueOf(resultJsonObj.get("stationOrd")));
            busArrivalInfo.setARRVEHLD(String.valueOf(resultJsonObj.get("arrvehld")));
            busArrivalInfo.setPLATENO(String.valueOf(resultJsonObj.get("plateNo")));
            busArrivalInfo.setPOSTPLATENO(String.valueOf(resultJsonObj.get("postPlateNo")));
            busArrivalInfo.setPREDICTTM(String.valueOf(resultJsonObj.get("predictTm")));
            busArrivalInfo.setREMAINSTATION(String.valueOf(resultJsonObj.get("remainStation")));
            busArrivalInfo.setGOVCD(String.valueOf(resultJsonObj.get("govCd")));
            busArrivalInfo.setGOVCDNM(String.valueOf(resultJsonObj.get("govCdNm")));

            BusArrivalInfo busArrivalInfo1 = busArrivalInfoRepository.save(busArrivalInfo);
        }

        log.info("kafka client(judyTopic) partition:{}, topicOffset:{}, topicName:{}, topicMessage{}", partition, topicOffset, topicName, topicMessage);
    }

}
