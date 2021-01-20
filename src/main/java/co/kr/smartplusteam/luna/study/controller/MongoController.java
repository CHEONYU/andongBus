package co.kr.smartplusteam.luna.study.controller;

import co.kr.smartplusteam.luna.study.vo.errorInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value="/mongo")
public class MongoController {

	@Autowired
	MongoTemplate mongoTemplate;

	@RequestMapping(value="/save", method=RequestMethod.GET)
	@ResponseBody
	public Object MongoSave(){
		log.info("MongoDB Save Start");
		errorInfo mongoVO = new errorInfo();
		mongoVO.setErrText("테스트 테스트");
		mongoTemplate.insert(mongoVO);

		return mongoVO;
	}
}
