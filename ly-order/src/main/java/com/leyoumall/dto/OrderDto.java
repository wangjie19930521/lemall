package com.leyoumall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @ClassName OrderDto
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-24
 * @Version V1.0
 **/
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    @NotNull
    private Long addressId;//收货人地址id
    @NotNull
    private Integer payType;//付款类型
    @NotNull
    private List<CartDto> carts;//购物车集合
}
