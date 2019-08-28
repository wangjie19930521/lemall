package com.leyoumall.item.mapper;

import com.leyoumall.common.mapper.BaseMapper;
import com.leyoumall.item.pojo.Stock;
import tk.mybatis.mapper.additional.insert.InsertListMapper;

//BaseMapper  自定义的mapper
//InsertListMapper<Stock>  这个的insertList()批量新增对于表id没有要求
//BaseMapper 中extends的InsertListMapper 的insertList()批量新增对于表id有要求  必须叫id
//BaseMapper可以改为继承没有要求的
public interface StockMapper extends BaseMapper<Stock>/*, InsertListMapper<Stock>*/ {
}
