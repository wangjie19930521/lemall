package com.leyoumall.item.controller;

import com.leyoumall.item.pojo.Brand;
import com.leyoumall.item.service.BrandService;
import com.leyoumall.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName BrandController
 * @Description:
 * @Author Administrator
 * @Date 2019-08-04
 * @Version V1.0
 **/
@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     *      * @MethodName: queryBrandByPage
     *      * @Description: 分页查询品牌
     *      * @Param: [page, rows, sortBy, desc, key]  第几页，页条数，根据什么排序，升序降序，搜索文本值
     *      * @Return: org.springframework.http.ResponseEntity<com.leyoumall.vo.PageResult<com.leyoumall.item.pojo.Brand>>
     *      * @Author: Administrator
     *      * @Date: 2019-08-04
     **/
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy",required = false) String sortBy,
            @RequestParam(value = "desc",defaultValue = "false") Boolean desc,
            @RequestParam(value = "key",required = false) String key){

        return ResponseEntity.ok(brandService.queryBrandByPage(page,rows,sortBy,desc,key));
    }

    /**
     * @MethodName: saveBrand
     * @Description: 新增品牌  ，同时维护品牌和分类中间表，一个品牌可以是多个分类
     * @Param: [brand, cids]
     * @Return: void
     * @Author: Administrator
     * @Date: 2019-08-05
     **/
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    /**
     * @MethodName: queryBrandByCid
     * @Description: 查询分类下的品牌  list
     * @Param: [cid]
     * @Return: org.springframework.http.ResponseEntity<java.util.List<com.leyoumall.item.pojo.Brand>>
     * @Author: wangJ1e
     * @Date: 2019-08-07
     **/
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandByid(@PathVariable("id") Long id){
        return ResponseEntity.ok(brandService.queryByid(id));
    }
    @GetMapping("brands")
    public ResponseEntity<List<Brand>> queryBrandByIds(@RequestParam("ids") List ids){
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }
}
