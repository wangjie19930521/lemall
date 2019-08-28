package com.leyoumall.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.item.mapper.BrandMapper;
import com.leyoumall.item.pojo.Brand;
import com.leyoumall.item.service.BrandService;
import com.leyoumall.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @ClassName BrandServiceImpl
 * @Description: BrandService
 * @Author Administrator
 * @Date 2019-08-04
 * @Version V1.0
 **/
@Service
public class
BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * @MethodName: queryBrandByPage
     * @Description: 分页查询品牌
     * @Param: [page, rows, sortBy, desc, key]  第几页，页条数，根据什么排序，升序降序，搜索文本值
     * @Return: com.leyoumall.vo.PageResult<com.leyoumall.item.pojo.Brand>
     * @Author: Administrator
     * @Date: 2019-08-04
     **/
    @Override
    public PageResult<Brand> queryBrandByPage(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Brand.class);
        if(StringUtils.isNoneBlank(key)){
            //创建条件   name  like   或者  letter 等于
            example.createCriteria().orLike("name","%"+key+"%")
                    .orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc ? "DESC" : "ASC");
            example.setOrderByClause(orderByClause);
        }
        //查询     brands其实是分页对象  因为pagehelper处理
        List<Brand> brands = brandMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        //解析分页结果
        PageInfo<Brand> info = new PageInfo<>(brands);
        return new PageResult<Brand>(info.getTotal(),brands);
    }

    /**
     * @MethodName: saveBrand
     * @Description: 新增品牌  ，同时维护品牌和分类中间表，一个品牌可以是多个分类
     * @Param: [brand, cids]
     * @Return: void
     * @Author: Administrator
     * @Date: 2019-08-05
     **/
    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //新增品牌  新增成功后id有了
        brand.setId(null);
        int count = brandMapper.insert(brand);
        if(count != 1){
            throw new LyException(ExceptionEnum.BRAND_SAVA_ERROR);
        }
        //新增中间表
        for (Long cid : cids) {
            count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if(count != 1){
                throw new LyException(ExceptionEnum.BRAND_SAVA_ERROR);
            }
        }
    }

    @Override
    public Brand queryByid(Long id) {
        Brand brand = brandMapper.selectByPrimaryKey(id);
        if(brand == null){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brand;
    }

    /**
     * @MethodName: queryBrandByCid
     * @Description: 查询分类下的所有品牌
     * @Param: [cid]
     * @Return: java.util.List<com.leyoumall.item.pojo.Brand>
     * @Author: wangJ1e
     * @Date: 2019-08-07
     **/
    @Override
    public List<Brand> queryBrandByCid(Long cid) {
        List<Brand> brands = brandMapper.selectBrandByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }

    @Override
    public List<Brand> queryBrandByIds(List ids) {
        List<Brand> brands = brandMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(brands)) {
            throw new LyException(ExceptionEnum.BRAND_NOT_FOUND);
        }
        return brands;
    }
}
