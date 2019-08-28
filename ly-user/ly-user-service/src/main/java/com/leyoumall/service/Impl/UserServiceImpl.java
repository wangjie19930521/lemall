package com.leyoumall.service.Impl;

import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.common.utils.NumberUtils;
import com.leyoumall.mapper.UserMapper;
import com.leyoumall.service.UserService;
import com.leyoumall.user.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserServiceImpl
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
@Service
public class
UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String KEY_PRE = "user:verify:code";

    @Override
    public Boolean checkData(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new LyException(ExceptionEnum.USER_DATA_INVOID);
        }
        return userMapper.selectCount(user) == 0;
    }

    @Override
    public void sendCode(String phone) {
        String key = KEY_PRE + phone;
        String code = NumberUtils.generateCode(6);
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);
        amqpTemplate.convertAndSend("leyou.sms.exchange","sms.verify.code",map);
        stringRedisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
    }

    @Override
    public void register(User user, String code) {
        /*我们这里会使用Hibernate-Validator框架完成数据校验：*/
        //检验验证码
        String cacheCode = stringRedisTemplate.opsForValue().get(KEY_PRE + user.getPhone());
        if (!StringUtils.equals(cacheCode,code)) {
            throw new LyException(ExceptionEnum.CODE_INVOID);
        }
        //对密码加密
        String salt = "";//随机字符串
        user.setSalt(salt);
        user.setPassword("");//todo 通过MD5加密，并用salt 增加密码复杂度
        user.setCreated(new Date());
        userMapper.insert(user);
    }

    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (user == null) {
            throw new LyException(ExceptionEnum.USERNAME_PASSWORD_INVOID);
        }
        //password  时间MD5加密  并且盐值
        if (StringUtils.equals(user.getPassword(),password)) {
            throw new LyException(ExceptionEnum.USERNAME_PASSWORD_INVOID);
        }
        return user;
    }


}
