package com.leyoumall.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @ClassName Sku
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-13
 * @Version V1.0
 **/
@Data
@Table(name = "tb_sku")
public class Sku {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long spuId;
    private String title;
    private String images;
    private Long price;
    private String ownSpec;//商品特殊规格键值对
    private String indexes;//商品特殊规格下标
    private Boolean enable;//是否可用  逻辑删除用
    private Date createTime; //创建时间
    private Date lastUpdateTime;//最后更新时间
    @Transient//不需要持久化  不会映射数据库字段
    private Integer stock;  // 库存

}
