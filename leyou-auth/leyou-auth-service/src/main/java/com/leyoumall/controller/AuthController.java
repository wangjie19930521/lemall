package com.leyoumall.controller;

import com.leyoumall.common.utils.CookieUtils;
import com.leyoumall.config.JwtProperties;
import com.leyoumall.entity.UserInfo;
import com.leyoumall.service.AuthService;
import com.leyoumall.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName AuthController
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-19
 * @Version V1.0
 **/
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    JwtProperties properties;
    /**
     * @MethodName: login
     * @Description: 登录授权
     * @Param: [username, password]
     * @Return: org.springframework.http.ResponseEntity<java.lang.String>
     * @Author: wangJ1e
     * @Date: 2019-08-19
     **/
    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password,
                                      HttpServletRequest request,
                                      HttpServletResponse response){
        String token = authService.login(username,password);
        //2.将token写入cookie，并指定httpOnly为true，防止通过js获取和修改
        //setCookie  里面有设置cookie的域，
        // 在nginx中设置   代理配置文件中
        // 设置最开始的域，最开始的域才有cookie，否则经过网关，或者nginx  之后，cookie没有写入最开始的域
        CookieUtils.setCookie(request,response,properties.getCookieName(),token,properties.getCookieMaxAge(),true);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    /**
     * 验证用户信息   每个页面的共同组件里面  有一个 monted函数，页面创建就发送验证，而不是每个请求的时候的判断
     * @param token
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("LY_TOKEN")String token, HttpServletRequest request, HttpServletResponse response){
        try {
            // 从token中解析token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.properties.getPublicKey());
            // 解析成功要重新刷新token
            token = JwtUtils.generateToken(userInfo, this.properties.getPrivateKey(), this.properties.getExpire());
            // 更新cookie中的token
            CookieUtils.setCookie(request, response, this.properties.getCookieName(), token, this.properties.getCookieMaxAge());
            // 解析成功返回用户信息
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 出现异常则，响应500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
