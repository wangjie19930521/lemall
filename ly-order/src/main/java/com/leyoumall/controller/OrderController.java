package com.leyoumall.controller;

import com.leyoumall.dto.OrderDto;
import com.leyoumall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName OrderController
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-24
 * @Version V1.0
 **/
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * @MethodName: createOrder
     * @Description:   todo  创建订单
     * @Param: [orderDto]
     * @Return: org.springframework.http.ResponseEntity<java.lang.Long>
     * @Author: wangJ1e
     * @Date: 2019-08-24
     **/
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto){
        Long id = orderService.createOrder(orderDto);
        return null;
    }
}
