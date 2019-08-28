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
 * @ClassName Spu
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-07
 * @Version V1.0
 **/
@Data
@Table(name = "tb_spu")
public class Spu {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private Long brandId;
    private Long cid1;//1级分类
    private Long cid2;//2级分类
    private Long cid3;//3级分类
    private String title;//标题
    private String subTitle;//副标题
    private Boolean saleable;//是否上架
    @JsonIgnore
    private Boolean valid;//是否有效删除  逻辑删除啊用
    private Date createTime; //创建时间
    @JsonIgnore  //返回的时候json字符串中不会有这个字段
    private Date lastUpdateTime; //最后一次更新时间
    @Transient
    private String bname;//品牌名称
    @Transient  //不需要持久化  不会映射数据库字段
    private String cname;//分类名称
    @Transient
    private SpuDetail spuDetail;
    @Transient
    private List<Sku> skus;
}
