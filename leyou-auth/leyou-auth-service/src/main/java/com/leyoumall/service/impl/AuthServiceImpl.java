package com.leyoumall.service.impl;

import com.leyoumall.client.UserClient;
import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.config.JwtProperties;
import com.leyoumall.entity.UserInfo;
import com.leyoumall.service.AuthService;
import com.leyoumall.user.pojo.User;
import com.leyoumall.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName AuthServiceImpl
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-19
 * @Version V1.0
 **/
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties properties;
    @Override
    public String login(String username, String password) {
        try {
            //1.调用微服务查询用户信息
            User user = userClient.queryUserByUsernameAndPassword(username, password);
            if (user == null) {
                throw new LyException(ExceptionEnum.USERNAME_PASSWORD_INVOID);
            }
            //生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()), properties.getPrivateKey(), properties.getExpire());
            return token;
        }catch (Exception e){
            log.error("生成token失败，用户:{}",username);
            throw new LyException(ExceptionEnum.TOKEN_FAIL);
        }
    }
}
