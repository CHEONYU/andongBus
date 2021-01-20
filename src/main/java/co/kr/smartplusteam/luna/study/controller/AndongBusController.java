package co.kr.smartplusteam.luna.study.controller;

import co.kr.smartplusteam.luna.study.Repository.BusStationInfoRepository;
import co.kr.smartplusteam.luna.study.service.AndongBusService;
import co.kr.smartplusteam.luna.study.vo.BusStationInfo;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Slf4j
@RestController
@RequestMapping(value="/andongbus")
public class AndongBusController {

    @Autowired
    private AndongBusService andongBusService;

    @Autowired
    BusStationInfoRepository busStationInfoRepository;


    //안동 버스정류장 목록 호출
    @RequestMapping( value="/stationListPaging" )
    public JSONObject andongBusStationList(int pageIndex) {
        JSONObject busStationList = andongBusService.andongBusStationList(pageIndex);
        return busStationList;
    }

    //안동 버스도착 적재API
    @RequestMapping(value = "/insertArrivalinfo")
    public JSONObject insertArrivalinfo(String stationId){
        JSONObject insertResult = andongBusService.insertArrivalinfo(stationId);
        return insertResult;
    }

    //안동 버스도착 조회API
    @RequestMapping(value = "/selectArrivalinfo")
    public JSONArray selectArrivalinfo(String stationId){
        JSONArray selectResult = andongBusService.selectArrivalinfo(stationId);
        return selectResult;
    }


}
