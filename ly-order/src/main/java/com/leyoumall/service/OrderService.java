package com.leyoumall.service;

import com.leyoumall.dto.OrderDto;

public interface OrderService {
    Long createOrder(OrderDto orderDto);
}
