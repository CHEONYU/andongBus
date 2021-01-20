package co.kr.smartplusteam.luna.study.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Service
public class RedisService {

    @Resource
    private RedisTemplate redisTemplate;

    //redis 저장
    public void redisSave(String stationId, String arrivalInfo) {
        log.info("Redis Save Start [{}]", arrivalInfo);
        redisTemplate.opsForValue().set(stationId, arrivalInfo,10, TimeUnit.MINUTES);
        log.info("Redis Save Success!![{}],[{}]", stationId, arrivalInfo);

    }

    //redis 조회
    public String redisSelect(Map stationId) {
        log.info("Redis Select Start");
        String result = (String)redisTemplate.opsForValue().get(stationId.get("stationId"));
        log.info("Redis Select Success!![{}],[{}]", stationId, result);
        return result;
    }
}
