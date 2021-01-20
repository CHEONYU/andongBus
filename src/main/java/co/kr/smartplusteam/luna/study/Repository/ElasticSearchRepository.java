package co.kr.smartplusteam.luna.study.Repository;

import co.kr.smartplusteam.luna.study.vo.RawData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


@Repository("ElasticSearchRepository")
public interface ElasticSearchRepository extends ElasticsearchRepository<RawData, String> {
}