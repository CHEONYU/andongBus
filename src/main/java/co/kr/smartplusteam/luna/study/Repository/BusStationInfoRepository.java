package co.kr.smartplusteam.luna.study.Repository;

import co.kr.smartplusteam.luna.study.vo.BusStationInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BusStationInfoRepository extends JpaRepository<BusStationInfo, String>{
    @Query("SELECT MAX(s.REGISTRATION_ID) FROM BusStationInfo s WHERE s.STATION_ID = :stationId")
    public String findRecentInputStation(@Param("stationId") String stationId);
}
