package com.leyoumall.api;

import com.leyoumall.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ClassName UserApi
 * @Description: TODO
 * @Author wangJ1e
 * @Date 2019-08-19
 * @Version V1.0
 **/
public interface UserApi {
    @GetMapping("query")
    User queryUserByUsernameAndPassword(@RequestParam("username") String username,
                                                               @RequestParam("password") String password);
}
