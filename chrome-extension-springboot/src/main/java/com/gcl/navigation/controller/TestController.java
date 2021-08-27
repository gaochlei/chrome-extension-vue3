package com.gcl.navigation.controller;

import com.gcl.navigation.domain.Test;
import com.gcl.navigation.domain.User;
import com.gcl.navigation.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Value("${test.hello:TEST}")
    private String testHello;
    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("hello")
    public String hello(){
        return "Hello world" + testHello;
    }

    @Resource
    private TestService testService;

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }


    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @GetMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }

    @GetMapping("/testRedis")
    public User testRedis() {
        User user = new User();
        user.setEmail("gsdfsagag@163.com");
        user.setName("lesdfsdfi");
        user.setPassword("pappp");
        //设置值到redis
        redisTemplate.opsForValue().set(1111,user);
        //从redis获取值
        User user1 = (User)redisTemplate.opsForValue().get(1111);
        return user1;
    }


}
