package co.kr.smartplusteam.luna.study.service;

import co.kr.smartplusteam.luna.study.vo.BusArrivalInfo;
import co.kr.smartplusteam.luna.study.vo.errorType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service("AndongBusService")
public class AndongBusService {

    @Autowired
    ElasticSearchService elasticSearchService;

    @Autowired
    RedisService redisService;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @Autowired
    MysqlService mysqlService;

    @Autowired
    MongoService mongoService;

    private static final ObjectMapper mapper = new ObjectMapper();

    //버스정류장 목록 호출
    public JSONObject andongBusStationList(int pageIndex){
        String body = "";
        JSONParser parser = new JSONParser();
        JSONObject jsonObj = new JSONObject();

        Map parameter = new HashMap();
        parameter.put("pageIndex", pageIndex);
        String apiurl = "http://bus.andong.go.kr:8080/api/facilities/station/getDataList";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("appication", "json", Charset.forName("UTF-8")));

            UriComponents builder = UriComponentsBuilder.fromHttpUrl(apiurl)
                    .queryParam("type", "Paging")
                    .queryParam("pageSize", "10")
                    .queryParam("pageUnit", "10")
                    .queryParam("pageIndex", pageIndex)
                    .build(false);    //자동으로 encode해주는 것을 막기 위해 false
            //안동버스API 호출
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
            String statusCode = response.getStatusCode().toString();
            if(statusCode.equals("200 OK")){
                //엘라스틱서치에 rawData 적재
                try {
                    elasticSearchService.elasticSearchSave(apiurl, parameter, response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    mongoService.mongoSave(errorType.COMPONENT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_ELASTIC, e.toString());
                }

                body = response.getBody().toString();
                Object bodyObj = parser.parse(body);
                jsonObj = (JSONObject) bodyObj;
            }else{
                mongoService.mongoSave(errorType.COMPONENT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_APICALL, response.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            mongoService.mongoSave(errorType.COMPONENT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_ETC, e.toString());
        }
        return jsonObj;
    }

    //도착정보 적재
    public JSONObject insertArrivalinfo(String stationId){

        log.info("insertArrivalinfo Start [{}]", stationId);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("stationId", stationId);

        JSONObject insertResult = new JSONObject();
        insertResult.put("mongo", "MONGO IS NOT USED");
        String apiurl = "http://bus.andong.go.kr:8080/api/facilities/station/getBusArriveData";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("appication", "json", Charset.forName("UTF-8")));

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(apiurl)
                .queryParam("stationId", stationId)
                .build(false);    //자동으로 encode해주는 것을 막기 위해 false

        //안동버스API 호출
        try{
            ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<String>(headers), String.class);
            String statusCode = response.getStatusCode().toString();

            //엘라스틱서치에 rawData 적재
            try {
                elasticSearchService.elasticSearchSave(apiurl, parameter, response.toString());
                insertResult.put("elasticSearch","ELASTICSEARCH INSERT SUCCESS");
            } catch (Exception e) {
                e.printStackTrace();
                insertResult.put("elasticSearch","ELASTICSEARCH INSERT FAIL");

                try {
                    mongoService.mongoSave(errorType.INSERT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_ELASTIC, e.toString());
                    insertResult.put("mongo", "MONGO INSERT SUCCESS");
                } catch (Exception exception) {
                    exception.printStackTrace();
                    insertResult.put("mongo", "MONGO INSERT FAIL");
                }
                ///return insertResult;
            }

            if(statusCode.equals("200 OK") && response.getBody().indexOf("\"code\" : 200") >= 0){
                insertResult.put("apiCall", "API CALL SUCCESS");
            }else{
                insertResult.put("apiCall", "API CALL FAIL");
                insertResult.put("redis", "REDIS IS STOPED");
                insertResult.put("kafka", "KAFKA IS STOPED");

                try {
                    mongoService.mongoSave(errorType.INSERT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_APICALL, response.toString());
                    insertResult.put("mongo", "MONGO INSERT SUCCESS");
                } catch (Exception e) {
                    e.printStackTrace();
                    insertResult.put("mongo", "MONGO INSERT FAIL");
                }
                return insertResult;
            }
            String arrivalinfo = response.getBody().toString();

            //Redis에 cache data 적재
            try {
                redisService.redisSave(stationId, arrivalinfo);
                insertResult.put("redis","REDIS INSERT SUCCESS");
            } catch (Exception e) {
                e.printStackTrace();
                insertResult.put("redis","REDIS INSERT FAIL");
                insertResult.put("kafka","KAFKA IS STOPED");
                try {
                    mongoService.mongoSave(errorType.INSERT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_REDIS, e.toString());
                    insertResult.put("mongo", "MONGO INSERT SUCCESS");
                } catch (Exception exception) {
                    exception.printStackTrace();
                    insertResult.put("mongo", "MONGO INSERT FAIL");
                }
                return insertResult;
            }

            try {
                //kafka에 message 적재
                kafkaProducerService.sendTopic("judy", arrivalinfo);
                insertResult.put("kafka","KAFKA TOPIC MESSAGE SUCCESS");
            } catch (Exception e) {
                e.printStackTrace();
                insertResult.put("kafka","KAFKA TOPIC MESSAGE FAIL");
                try {
                    mongoService.mongoSave(errorType.INSERT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_KAFKA, e.toString());
                    insertResult.put("mongo", "MONGO INSERT SUCCESS");
                } catch (Exception exception) {
                    exception.printStackTrace();
                    insertResult.put("mongo", "MONGO INSERT FAIL");
                }
                //return insertResult;
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                mongoService.mongoSave(errorType.INSERT_REQUEST, apiurl, parameter, errorType.MONGO_ERR_TYPE_ETC, e.toString());
                insertResult.put("mongo", "MONGO INSERT SUCCESS");
            } catch (Exception exception) {
                exception.printStackTrace();
                insertResult.put("mongo", "MONGO INSERT FAIL");
            }
        }

        return insertResult;
    }

    //버스 도착정보 조회
    public JSONArray selectArrivalinfo(String stationId){
        log.info("selectArrivalinfo Start [{}]", stationId);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("stationId", stationId);

        JSONArray selectResult = new JSONArray();
        try {
            //redis 캐싱값 조회
            String redisResult = null;
            try {
                redisResult = redisService.redisSelect(parameter);
            } catch (Exception e) {
                e.printStackTrace();
                mongoService.mongoSave(errorType.SELECT_REQUEST, "", parameter, errorType.MONGO_ERR_TYPE_REDIS, e.toString());
            }
            if(redisResult != null){
                //String to Json
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(redisResult);
                JSONObject jsonObj = (JSONObject) obj;

                JSONParser parserResult = new JSONParser();
                Object resultobj = parserResult.parse(jsonObj.get("results").toString());
                selectResult = (JSONArray) resultobj;
            }else{
                //redis 캐싱값 없으면 mysql 조회
                String regiId = null;
                try {
                    //redis 캐싱값 없으면 mysql 조회
                    regiId = mysqlService.mysqlSelectRecentBusStation(parameter);
                } catch (Exception e) {
                    e.printStackTrace();
                    mongoService.mongoSave(errorType.SELECT_REQUEST, "", parameter, errorType.MONGO_ERR_TYPE_MYSQL, e.toString());
                }
                if(regiId != null) {
                    parameter.put("regiId",regiId);
                    List<BusArrivalInfo> busArrivalInfo = null;
                    try {
                        busArrivalInfo = mysqlService.mysqlSelectBusArrivalInfo(regiId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mongoService.mongoSave(errorType.SELECT_REQUEST, "", parameter, errorType.MONGO_ERR_TYPE_MYSQL, e.toString());
                    }

                    for (int i = 0; i < busArrivalInfo.size(); i++) {
                        JSONObject jsonObj= new JSONObject();
                        jsonObj.put("routeId", busArrivalInfo.get(i).getROUTEID());
                        jsonObj.put("routeNum", busArrivalInfo.get(i).getROUTENUM());
                        jsonObj.put("routeNm", busArrivalInfo.get(i).getROUTENM());
                        jsonObj.put("via", busArrivalInfo.get(i).getVIA());
                        jsonObj.put("stationOrd", busArrivalInfo.get(i).getSTATIONORD());
                        jsonObj.put("arrvehld", busArrivalInfo.get(i).getARRVEHLD());
                        jsonObj.put("plateNo", busArrivalInfo.get(i).getPLATENO());
                        jsonObj.put("postPlateNo", busArrivalInfo.get(i).getPOSTPLATENO());
                        jsonObj.put("predictTm", busArrivalInfo.get(i).getPREDICTTM());
                        jsonObj.put("remainStation", busArrivalInfo.get(i).getREMAINSTATION());
                        jsonObj.put("govCd", busArrivalInfo.get(i).getGOVCD());
                        jsonObj.put("govCdNm", busArrivalInfo.get(i).getGOVCDNM());

                        selectResult.add(i, jsonObj);;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mongoService.mongoSave(errorType.SELECT_REQUEST, "", parameter, errorType.MONGO_ERR_TYPE_ETC, e.toString());
        }

        return selectResult;
    }
}
