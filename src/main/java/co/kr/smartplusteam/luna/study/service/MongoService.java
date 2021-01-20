package co.kr.smartplusteam.luna.study.service;

import co.kr.smartplusteam.luna.study.vo.errorInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class MongoService {

    @Autowired
    MongoTemplate mongoTemplate;

    public void mongoSave(String reqDiv, String reqUrl, Map parameter, String errType, String errText){
        log.info("MongoDB Save Start [{}],[{}],[{}],[{}],[{}]",reqDiv, reqUrl, parameter, errType, errText);

        errorInfo errorInfo = new errorInfo();
        errorInfo.setLog_date(new Date());
        errorInfo.setReqDiv(reqDiv);
        errorInfo.setReqUrl(reqUrl);
        errorInfo.setParameter(parameter);
        errorInfo.setErrType(errType);
        errorInfo.setErrText(errText);
        mongoTemplate.insert(errorInfo);

        log.info("MongoDB Save Success!! [{}]", errType);
    }

}
