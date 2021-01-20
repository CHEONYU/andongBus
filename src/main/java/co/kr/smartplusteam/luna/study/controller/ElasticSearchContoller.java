package co.kr.smartplusteam.luna.study.controller;

import co.kr.smartplusteam.luna.study.Repository.ElasticSearchRepository;
import co.kr.smartplusteam.luna.study.vo.RawData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value="/es")
public class ElasticSearchContoller {

	@Resource
	private ElasticSearchRepository elasticSearchRepository;

	@RequestMapping( value="/save" )
	public void elasticSearchSave() {
		log.info("Elasticsearch Save Start");

		RawData rawData = new RawData();
		rawData.setId(UUID.randomUUID().toString());
		rawData.setApiUrl("apiurl");
		rawData.setRawData("fdfdf");

		elasticSearchRepository.save(rawData);
	}
}
