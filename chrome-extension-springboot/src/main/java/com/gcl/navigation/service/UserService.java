package com.gcl.navigation.service;

import com.gcl.navigation.domain.User;
import com.gcl.navigation.domain.UserExample;
import com.gcl.navigation.exception.BusinessException;
import com.gcl.navigation.exception.BusinessExceptionCode;
import com.gcl.navigation.mapper.UserMapper;
import com.gcl.navigation.req.UserQueryReq;
import com.gcl.navigation.req.UserSaveReq;
import com.gcl.navigation.resp.UserLoginResp;
import com.gcl.navigation.util.CopyUtil;
import com.gcl.navigation.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;
    /**
    * @Description: 用户登录保存
    * @Param:
    * @return:
    * @Author: gaochlei
    * @Date: 2021/7/18
    */
    public void save(UserSaveReq req) {
        User userDb = selectByEmail(req.getEmail());
        if (ObjectUtils.isEmpty(userDb)) {
            req.setId(snowFlake.nextId());
            User user = CopyUtil.copy(req, User.class);
            userMapper.insert(user);
        } else {
            LOG.info("该邮箱已经注册 {}",req.getEmail());
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_EMAIL_EXIST);
        }
    }
    /** 
    * @Description: 登录 
    * @Param: UserSaveReq 
    * @return: UserLoginResp 
    * @Author: gaochlei
    * @Date: 2021/7/21
    */ 
    public UserLoginResp login(UserQueryReq req) {
        User user = selectByEmail(req.getEmail());
        if (ObjectUtils.isEmpty(user)) {
            LOG.info(("用户不存在：{}"),req.getEmail());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if(user.getPassword().equals(req.getPassword())) {
                UserLoginResp userLoginResp = CopyUtil.copy(user, UserLoginResp.class);
                return userLoginResp;
            }else {
                LOG.info("密码不对：{}",req.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    public User selectByEmail(String email){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if(CollectionUtils.isEmpty(users)){
            return null;
        }else {
            return users.get(0);
        }
    }
}
