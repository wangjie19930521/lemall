package com.leyoumall.item.service;

import com.leyoumall.item.pojo.Category;

import java.util.List;

public interface CategoryService {

    List<Category> queryCategoryListByPid(Long pid);

    List queryCategoryidByBrandid(String bid);

    List<Category> queryCategoryByIds(List ids);
}
