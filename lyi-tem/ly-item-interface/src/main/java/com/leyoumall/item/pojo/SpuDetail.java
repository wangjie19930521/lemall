package com.leyoumall.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName SpuDetail
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-07
 * @Version V1.0
 **/
@Data
@Table(name = "tb_spudetail")
public class SpuDetail {
    @Id
    private Long spuId;//id
    private String descripyion;// 商品描述
    private String specialSpec;//商品特殊规格的名称和可选值模板
    private String GenerricSpec;//商品的全局规格属性  通用参数的值
    private String packingList;//包装清单
    private String afterService;//售后服务

}
