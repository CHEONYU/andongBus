package co.kr.smartplusteam.luna.study.controller;

import co.kr.smartplusteam.luna.study.vo.RedisVO;
import co.kr.smartplusteam.luna.study.Repository.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(value="/redis")
public class RedisController {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping( value="/save" )
    public void RedisSave() {
        log.info("Redis Save Start");

        String id = "judy";
        LocalDateTime refreshTime = LocalDateTime.of(2018, 5, 26, 0, 0);
        RedisVO redisVO = RedisVO.builder()
                .id(id)
                .amount(1000L)
                .refreshTime(refreshTime)
                .build();

        redisRepository.save(redisVO);
        redisTemplate.opsForValue().set("test", "redistest");
    }
}
