package co.kr.smartplusteam.luna.study.Repository;

import co.kr.smartplusteam.luna.study.vo.BusArrivalInfo;
import co.kr.smartplusteam.luna.study.vo.BusStationInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusArrivalInfoRepository extends JpaRepository<BusArrivalInfo, String>{
    List<BusArrivalInfo> findAllByPARENTREGISTRATIONID(String regiId);
}
