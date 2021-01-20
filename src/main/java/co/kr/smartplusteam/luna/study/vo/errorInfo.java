package co.kr.smartplusteam.luna.study.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@Document(collation = "andongBusError")
public class errorInfo implements Serializable {

    @Field("log_date")
    private Date log_date;

    @Field("req_div")
    private String reqDiv;

    @Field("req_url")
    private String reqUrl;

    @Field("parameter")
    private Map parameter;

    @Field("err_type")
    private String errType;

    @Field("err_text")
    private String errText;


}
