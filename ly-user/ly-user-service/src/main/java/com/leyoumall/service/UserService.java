package com.leyoumall.service;

import com.leyoumall.user.pojo.User;

public interface UserService {
    Boolean checkData(String data, Integer type);

    void sendCode(String phone);

    void register(User user, String code);

    User queryUserByUsernameAndPassword(String username, String password);
}
