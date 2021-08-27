package com.gcl.navigation.controller;

import com.alibaba.fastjson.JSONObject;
import com.gcl.navigation.domain.User;
import com.gcl.navigation.exception.BusinessException;
import com.gcl.navigation.exception.BusinessExceptionCode;
import com.gcl.navigation.req.UserQueryReq;
import com.gcl.navigation.req.UserSaveReq;
import com.gcl.navigation.resp.CommonResp;
import com.gcl.navigation.resp.UserLoginResp;
import com.gcl.navigation.service.UserService;
import com.gcl.navigation.util.CopyUtil;
import com.gcl.navigation.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserService userService;

    @Resource
    private SnowFlake snowFlake;
    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserQueryReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp();
        try {
            UserLoginResp userLoginResp = userService.login(req);
            resp.setMessage("登录成功");
            resp.setContent(userLoginResp);

            Long token = snowFlake.nextId();
            LOG.info("生成单点登录token：{}，并放入redis中", token);
            userLoginResp.setToken(token.toString());
            redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp) , 3600 * 24, TimeUnit.SECONDS);

        } catch (BusinessException e) {
            resp.setSuccess(false);
            resp.setMessage(BusinessExceptionCode.LOGIN_USER_ERROR.getDesc());
        }
        return resp;
    }

    @PostMapping("/add")
    public CommonResp add(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp();
        try {
            userService.save(req);
            resp.setMessage("注册成功");
        }catch (BusinessException e){
            resp.setSuccess(false);
            resp.setMessage(BusinessExceptionCode.USER_LOGIN_EMAIL_EXIST.getDesc());
        }
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token){
        CommonResp resp = new CommonResp();
        redisTemplate.delete(token);
        LOG.info("从redis删除token:{ }",token);
        resp.setMessage("退出登录成功");
        return resp;
    }

}
