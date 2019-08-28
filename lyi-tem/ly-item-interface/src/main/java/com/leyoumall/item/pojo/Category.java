package com.leyoumall.item.pojo;

import tk.mybatis.mapper.annotation.KeySql;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="tb_category")
public class Category {
    /*useGeneratedKeys设置为 true 时，表示如果插入的表id以自增列为主键，
    则允许 JDBC 支持自动生成主键，并可将自动生成的主键id返回。
    useGeneratedKeys参数只针对 insert 语句生效，默认为 false；*/
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent; // 注意isParent生成的getter和setter方法需要手动加上Is
    private Integer sort;
    // getter和setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean getParent() {
        return isParent;
    }

    public void setParent(Boolean parent) {
        isParent = parent;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
