package com.leyoumall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName CartDto
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-24
 * @Version V1.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long skuId;

    private Integer num;

}
