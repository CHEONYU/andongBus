package co.kr.smartplusteam.luna.study.Repository;

import co.kr.smartplusteam.luna.study.vo.RedisVO;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisVO, String> {
}
