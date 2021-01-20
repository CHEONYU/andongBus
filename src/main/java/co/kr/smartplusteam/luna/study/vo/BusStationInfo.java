package co.kr.smartplusteam.luna.study.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name="bus_station_info")
public class BusStationInfo implements Serializable {
    @Id
    @Column(name = "REGISTRATION_ID")
    private String REGISTRATION_ID;
    @Column(name = "STATION_ID")
    private String STATION_ID;
    @Column(name = "COUNT")
    private String COUNT;

}
