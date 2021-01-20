package co.kr.smartplusteam.luna.study.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="bus_arrival_info")
public class BusArrivalInfo implements Serializable {
    @Id
    @Column(name = "REGISTRATION_ID")
    private String REGISTRATION_ID;
    @Column(name = "PARENT_REGISTRATION_ID")
    private String PARENTREGISTRATIONID;
    @Column(name = "STATION_ID")
    private String STATION_ID;
    @Column(name = "ROUTEID")
    private String ROUTEID;
    @Column(name = "ROUTENUM")
    private String ROUTENUM;
    @Column(name = "ROUTENM")
    private String ROUTENM;
    @Column(name = "VIA")
    private String VIA;
    @Column(name = "STATIONORD")
    private String STATIONORD;
    @Column(name = "ARRVEHLD")
    private String ARRVEHLD;
    @Column(name = "PLATENO")
    private String PLATENO;
    @Column(name = "POSTPLATENO")
    private String POSTPLATENO;
    @Column(name = "PREDICTTM")
    private String PREDICTTM;
    @Column(name = "REMAINSTATION")
    private String REMAINSTATION;
    @Column(name = "GOVCD")
    private String GOVCD;
    @Column(name = "GOVCDNM")
    private String GOVCDNM;

    public void setREGISTRATION_ID(String REGISTRATION_ID) {
        this.REGISTRATION_ID = REGISTRATION_ID = REGISTRATION_ID.equals("null") ? "" : REGISTRATION_ID;
    }

    public void setPARENT_REGISTRATION_ID(String PARENTREGISTRATIONID) {
        this.PARENTREGISTRATIONID = PARENTREGISTRATIONID = PARENTREGISTRATIONID.equals("null") ? "" : PARENTREGISTRATIONID;
    }

    public void setSTATION_ID(String STATION_ID) {
        this.STATION_ID = STATION_ID = STATION_ID.equals("null") ? "" : STATION_ID;
    }

    public void setROUTEID(String ROUTEID) {
        this.ROUTEID = ROUTEID = ROUTEID.equals("null") ? "" : ROUTEID;
    }

    public void setROUTENUM(String ROUTENUM) {
        this.ROUTENUM = ROUTENUM = ROUTENUM.equals("null") ? "" : ROUTENUM;
    }

    public void setROUTENM(String ROUTENM) {
        this.ROUTENM = ROUTENM = ROUTENM.equals("null") ? "" : ROUTENM;
    }

    public void setVIA(String VIA) {
        this.VIA = VIA = VIA.equals("null") ? "" : VIA;
    }

    public void setSTATIONORD(String STATIONORD) {
        this.STATIONORD = STATIONORD = STATIONORD.equals("null") ? "" : STATIONORD;
    }

    public void setARRVEHLD(String ARRVEHLD) {
        this.ARRVEHLD = ARRVEHLD = ARRVEHLD.equals("null") ? "" : ARRVEHLD;
    }

    public void setPLATENO(String PLATENO) {
        this.PLATENO = PLATENO = PLATENO.equals("null") ? "" : PLATENO;
    }

    public void setPOSTPLATENO(String POSTPLATENO) {
        this.POSTPLATENO = POSTPLATENO = POSTPLATENO.equals("null") ? "" : POSTPLATENO;
    }

    public void setPREDICTTM(String PREDICTTM) {
        this.PREDICTTM = PREDICTTM = PREDICTTM.equals("null") ? "" : PREDICTTM;
    }

    public void setREMAINSTATION(String REMAINSTATION) {
        this.REMAINSTATION = REMAINSTATION = REMAINSTATION.equals("null") ? "" : REMAINSTATION;
    }

    public void setGOVCD(String GOVCD) {
        this.GOVCD = GOVCD = GOVCD.equals("null") ? "" : GOVCD;
    }

    public void setGOVCDNM(String GOVCDNM) {
        this.GOVCDNM = GOVCDNM = GOVCDNM.equals("null") ? "" : GOVCDNM;
    }
}
