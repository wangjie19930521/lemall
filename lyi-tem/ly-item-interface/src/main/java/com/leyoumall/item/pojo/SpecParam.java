package com.leyoumall.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName SpecParam
 * @Description:
 * @Author Administrator
 * @Date 2019-08-06
 * @Version V1.0
 **/
@Data
@Table(name = "tb_specparam")
public class SpecParam {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private Long cid;

    private long groupid;

    private String name;

    @Column(name = "`numeric`")
    private Boolean numeric;

    private String unit;

    private Boolean generic;

    private Boolean searching;

    private String segments;
}
