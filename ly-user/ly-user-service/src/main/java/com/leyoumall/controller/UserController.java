package com.leyoumall.controller;

import com.leyoumall.service.UserService;
import com.leyoumall.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @ClassName UserController
 * @Description: TODO
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check/{data/{type}}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data" )String data,
                                             @PathVariable("type" )Integer type){
        return ResponseEntity.ok(userService.checkData(data,type));
    }

    @PostMapping("code")
    private ResponseEntity<Void> sendCode(@RequestParam("phone") String phone){
        userService.sendCode(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("register")
    public ResponseEntity<Void> registre(@Valid User user, @RequestParam("code") String code){
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("query")
    public ResponseEntity<User> queryUserByUsernameAndPassword(@RequestParam("username") String username,
                                                                @RequestParam("password") String password){
        return ResponseEntity.ok(userService.queryUserByUsernameAndPassword(username,password));
    }
}
