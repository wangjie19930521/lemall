package com.leyoumall.item.mapper;

import com.leyoumall.common.mapper.BaseMapper;
import com.leyoumall.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper extends BaseMapper<Brand>/*Mapper<Brand>*/ {

    @Insert("INSERT INTO tb_category_brand (category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertCategoryBrand(@Param("cid") Long cid,@Param("bid") Long bid);

    @Select("select * from tb_brand a,tb_category_brand b where a.id=b.brandid and b.categoeyid=#{cid}")
    List<Brand> selectBrandByCid(@Param("cid") Long cid);
}
