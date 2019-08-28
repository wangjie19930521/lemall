package com.leyoumall.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

/**
 * @ClassName BaseMapper
 * @Description: 通用化mapper,
 * @Author wangJ1e
 * @Date 2019-08-13
 * @Version V1.0
 **/
@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>, IdListMapper<T,Long>, InsertListMapper<T> {
}
