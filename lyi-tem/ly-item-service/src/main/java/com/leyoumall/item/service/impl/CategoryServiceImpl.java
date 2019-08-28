package com.leyoumall.item.service.impl;

import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.item.mapper.CategoryMapper;
import com.leyoumall.item.pojo.Category;
import com.leyoumall.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * @MethodName: queryCategoryListByPid
     * @Description: 根据商品分类pid查询分类表，查出下一级分类
     * @Param: [pid]
     * @Return: java.util.List<com.leyoumall.item.pojo.Category>
     * @Author: Administrator
     * @Date: 2019-08-04
     **/
    public List<Category> queryCategoryListByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        //categoryMapper.select(category)  将category中非空字段作为条件查询
        List<Category> list = categoryMapper.select(category);
        //判断结果
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }

    /**
     * @MethodName: queryCategoryidByBrandid
     * @Description: 根据品牌id查询分类id  ，返回的是分类对象的List   一个品牌可以是多个分类
     * @Param: [bid]
     * @Return: java.util.List
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @Override
    public List queryCategoryidByBrandid(String bid) {
        List list = categoryMapper.queryCategoryidByBrandid(bid);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }
    /**
     * @MethodName: queryCategoryByIds
     * @Description: 根据分类id集合查询出分类对象集合
     * @Param: [ids]
     * @Return: java.util.List<com.leyoumall.item.pojo.Category>
     * @Author: wangJ1e
     * @Date: 2019-08-15
     **/
    @Override
    public List<Category> queryCategoryByIds(List ids) {
        List<Category> list = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return list;
    }
}
