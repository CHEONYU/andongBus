package co.kr.smartplusteam.luna.study.controller;

import co.kr.smartplusteam.luna.study.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value="/kafka")
public class KafkaProductController {


	@Resource
	KafkaProducerService kafkaProducerService;

	@RequestMapping(value="/topic", method=RequestMethod.GET)
	public Object checker(){

		kafkaProducerService.sendTopic("judy", "발행 데이터");

		return "kafka topic success";

	}
}
