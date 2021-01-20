package co.kr.smartplusteam.luna.study.service;

import co.kr.smartplusteam.luna.study.vo.RawData;
import co.kr.smartplusteam.luna.study.Repository.ElasticSearchRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class ElasticSearchService {

	@Resource
	private ElasticSearchRepository elasticSearchRepository;

	//ElasticSearch 저장
	public void elasticSearchSave(String apiurl, Map<String, Object> parameters, String rawdata) {
		log.info("Elasticsearch Save Start [{}],[{}],[{}]", apiurl, parameters, rawdata);

		RawData rawData = new RawData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		rawData.setId(UUID.randomUUID().toString());
		rawData.setApiUrl(apiurl);
		rawData.setParameters(parameters.toString());
		rawData.setRegiTime(sdf.format(new Date()));
		rawData.setRawData(rawdata);

		log.info("Elasticsearch Save Success!!");
		elasticSearchRepository.save(rawData);
	}
}
