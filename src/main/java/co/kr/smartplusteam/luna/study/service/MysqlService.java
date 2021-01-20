package co.kr.smartplusteam.luna.study.service;

import co.kr.smartplusteam.luna.study.Repository.BusArrivalInfoRepository;
import co.kr.smartplusteam.luna.study.Repository.BusStationInfoRepository;
import co.kr.smartplusteam.luna.study.vo.BusArrivalInfo;
import co.kr.smartplusteam.luna.study.vo.BusStationInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class MysqlService {
    @Resource
    private BusStationInfoRepository busStationInfoRepository;

    @Resource
    private BusArrivalInfoRepository busArrivalInfoRepository;

    //mysql 조회
    public String mysqlSelectRecentBusStation(Map parameter) {
        log.info("mysql Select Recent Station Start [{}]", parameter);
        //가장 최근에 저장된 버스정류장 조회
        String regiId = busStationInfoRepository.findRecentInputStation(parameter.get("stationId").toString());
        log.info("mysql Select Success!! [{}]", regiId);
        return regiId;
    }

    public List<BusArrivalInfo> mysqlSelectBusArrivalInfo(String regiId) {
        log.info("mysql Select Bus Arrival info Start [{}]", regiId);
        //조회된 registration_id로 버스도착목록 조회
        List<BusArrivalInfo> busArrivalInfo= busArrivalInfoRepository.findAllByPARENTREGISTRATIONID(regiId);
        log.info("mysql Select Success!! [{}]", busArrivalInfo);
        return busArrivalInfo;
    }
}
