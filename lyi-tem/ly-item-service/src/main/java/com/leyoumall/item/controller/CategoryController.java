package com.leyoumall.item.controller;

import com.leyoumall.item.pojo.Category;
import com.leyoumall.item.service.CategoryService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    /**
     * @MethodName: queryCategoryListByPid
     * @Description: 根据商品分类pid查询分类表，查出下一级分类
     * @Param: [pid]
     * @Return: java.util.List<com.leyoumall.item.pojo.Category>
     * @Author: Administrator
     * @Date: 2019-08-04
     **/
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoryListByPid(@RequestParam("pid")Long pid){
        return ResponseEntity.ok(categoryService.queryCategoryListByPid(pid));
    }
    /**
     * @MethodName: queryCategoryidByBrandid
     * @Description: 根据品牌id查询分类id  ，返回的是分类对象的List   一个品牌可以是多个分类
     * @Param: [bid]
     * @Return: org.springframework.http.ResponseEntity<java.util.List>
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @GetMapping("list/{bid}")
    public ResponseEntity<List> queryCategoryidByBrandid(@PathVariable("bid") String bid){
        return ResponseEntity.ok(categoryService.queryCategoryidByBrandid(bid));
    }
    /**
     * @MethodName: queryCategoryByIds
     * @Description: 根据分类id集合查询出分类对象集合
     * @Param: [ids]
     * @Return: org.springframework.http.ResponseEntity<java.util.List<com.leyoumall.item.pojo.Category>>
     * @Author: wangJ1e
     * @Date: 2019-08-15
     **/
    @GetMapping("/list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(categoryService.queryCategoryByIds(ids));

    }
}
