package com.leyou.service.impl;

import com.leyou.interceptor.UserInterceptor;
import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.common.utils.JsonUtils;
import com.leyoumall.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName CartServiceImpl
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-21
 * @Version V1.0
 **/
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PRE = "cart:uid";

    @Override
    public void addCart(Cart cart) {
        //获取登录用户
        UserInfo loginUser = UserInterceptor.getLoginUser();
        String key = KEY_PRE + loginUser.getId();
        /*第一个key是用户id,value是一个map,map是购物车，key是skuid, value是CART 对象*/
        //hashkey  就是购物车item对象
        String hashKey = cart.getSkuId().toString();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        if (operation.hasKey(hashKey)) {
            //存在  修改数量
            String json = operation.get(hashKey).toString();
            Cart cacheCart = JsonUtils.parse(json, Cart.class);
            cart.setNum(cart.getNum() + cacheCart.getNum());
        }
        //写回redis  和//新增  都是下面这句    todo redis里面都是放String
        operation.put(hashKey,JsonUtils.serialize(cart));
    }

    @Override
    public List<Cart> queryCartList() {
        //获取登录用户
        UserInfo loginUser = UserInterceptor.getLoginUser();
        String key = KEY_PRE + loginUser.getId();
        if (!redisTemplate.hasKey(key)) {
            //外层key都没有  肯定没有  直接返回
            throw new LyException(ExceptionEnum.CART_NOT_FOUND);
        }
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        // todo  操作List中的对象，注意用流
        List<Cart> carts = operations.values().stream().map(o -> JsonUtils.parse(o.toString(), Cart.class))
                .collect(Collectors.toList());
        return carts;
    }
    /**
     * @MethodName: updateCartList
     * @Description: 前台购物车列表，点击加减号控制 购物车信息
     * @Param: [skuid, num]
     * @Return: void
     * @Author: wangJ1e
     * @Date: 2019-08-23
     **/
    @Override
    public void updateCartList(Long skuid, Integer num) {
        //获取登录用户
        UserInfo loginUser = UserInterceptor.getLoginUser();
        String key = KEY_PRE + loginUser.getId();
        BoundHashOperations<String, Object, Object> operation = redisTemplate.boundHashOps(key);
        //取出缓存中的该对象
        Cart cart = JsonUtils.parse(operation.get(skuid.toString()).toString(), Cart.class);
        cart.setNum(num);
        //写回redis
        operation.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));
    }

    @Override
    public void deleteCartList(Long skuid) {
        UserInfo user = UserInterceptor.getLoginUser();
        String key = KEY_PRE + user.getId();
        //删除直接用opsForHash  就行了
        this.redisTemplate.opsForHash().delete(key);
    }
}
