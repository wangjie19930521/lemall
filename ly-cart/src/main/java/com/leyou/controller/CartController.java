package com.leyou.controller;

import com.leyou.pojo.Cart;
import com.leyou.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @ClassName CartController
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-21
 * @Version V1.0
 **/
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart")
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Cart>> queryCartList(){
        return ResponseEntity.ok(cartService.queryCartList());
    }

    @PutMapping("/list")
    public ResponseEntity<Void> updateCartList(@RequestParam("skuid") Long skuid,@RequestParam("num") Integer num){
        cartService.updateCartList(skuid,num);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("list/{skuid}")
    public ResponseEntity<Void> deleteCartList(@PathVariable("skuid") Long skuid){
        cartService.deleteCartList(skuid);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
