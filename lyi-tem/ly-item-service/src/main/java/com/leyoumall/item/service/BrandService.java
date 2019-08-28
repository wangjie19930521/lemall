package com.leyoumall.item.service;

import com.leyoumall.item.pojo.Brand;
import com.leyoumall.vo.PageResult;

import java.util.List;

public interface BrandService {
    PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    void saveBrand(Brand brand, List<Long> cids);

    Brand queryByid(Long id);

    List<Brand> queryBrandByCid(Long cid);

    List<Brand> queryBrandByIds(List ids);
}
