package com.leyoumall.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName Stock
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-13
 * @Version V1.0
 **/
@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock;//秒杀可用库存
    private Integer seckillTotal;//已秒杀数量
    private Integer stock;//正常库存
}
