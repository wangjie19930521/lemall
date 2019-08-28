package com.leyoumall.item.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @ClassName SpecGroup
 * @Description: 规格参数组
 * @Author Administrator
 * @Date 2019-08-06
 * @Version V1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "tb_specgroup")
public class SpecGroup {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    //商品分类id
    private Long cid;
    //组名
    private String name;

    @Transient
    private List<SpecParam> params; // 该组下的所有规格参数集合
}
