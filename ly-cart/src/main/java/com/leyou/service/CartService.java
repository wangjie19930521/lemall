package com.leyou.service;

import com.leyou.pojo.Cart;

import java.util.List;

public interface CartService {
    void addCart(Cart cart);

    List<Cart> queryCartList();

    void updateCartList(Long skuid, Integer num);

    void deleteCartList(Long skuid);

}
