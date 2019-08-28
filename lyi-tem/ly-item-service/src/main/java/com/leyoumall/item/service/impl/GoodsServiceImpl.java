package com.leyoumall.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.item.mapper.SkuMapper;
import com.leyoumall.item.mapper.SpuDetailMapper;
import com.leyoumall.item.mapper.SpuMapper;
import com.leyoumall.item.mapper.StockMapper;
import com.leyoumall.item.pojo.*;
import com.leyoumall.item.service.BrandService;
import com.leyoumall.item.service.CategoryService;
import com.leyoumall.item.service.GoodsService;
import com.leyoumall.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName GoodsServiceImpl
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-07
 * @Version V1.0
 **/
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;



    /**
     * @MethodName: querySpuByPage
     * @Description: TODO   查询spu分页 ，多看此处
     * @Param: [page, rows, saleable, key]
     * @Return: com.leyoumall.vo.PageResult<com.leyoumall.item.pojo.Spu>
     * @Author: wangJ1e
     * @Date: 2019-08-07
     **/
    @Override
    public PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Spu.class);
        //criteria用于添加条件
        Example.Criteria criteria = example.createCriteria();
        //搜索字段条件
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%");
        }
        //是否上架条件
        if(saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }
        //默认按时间排序
        example.setOrderByClause("lastUpdateTime DESC");  //相当于  order by lastUpdateTime DESC
        List<Spu> spus = spuMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(spus)){
            throw new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        //解析分类和品牌名称
        loadCategoryAndBrandName(spus);
        //解析分页结果
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);

        return new PageResult<>(pageInfo.getTotal(),spus);
    }

    /**
     * @MethodName: saveGoods
     * @Description: 保存商品 TODO 此处多看
     * @Param: [spu]
     * @Return: void
     * @Author: wangJ1e
     * @Date: 2019-08-13
     **/
    @Override
    @Transactional
    public void saveGoods(Spu spu) {
        //新增spu
        spu.setId(null);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spu.setSaleable(true);
        spu.setValid(false);
        int count = spuMapper.insert(spu);
        if(count != 1){
            throw new LyException(ExceptionEnum.GOODS_SAVE_FAIL);
        }
        //新增detail
        SpuDetail spuDetail = spu.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);
        saveSkuAndStock(spu);
    }

    private void saveSkuAndStock(Spu spu) {
        int count;//定义库存集合
        List<Stock> stockList = new ArrayList<>();
        //新增sku
        List<Sku> skus = spu.getSkus();
        for (Sku sku : skus) {
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            sku.setSpuId(spu.getId());
            count = skuMapper.insert(sku);
            if(count != 1){
                throw new LyException(ExceptionEnum.GOODS_SAVE_FAIL);
            }

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            //先将库存放到集合  再批量写入
            stockList.add(stock);
        }
        //新增库存   根据集合批量新增
        count = stockMapper.insertList(stockList);
        if(count != stockList.size()){
            throw new LyException(ExceptionEnum.GOODS_SAVE_FAIL);
        }
    }

    /**
     * @MethodName: querySpuDetailBySpuid
     * @Description: 根据spuid查询SpuDetail
     * @Param: [spuid]
     * @Return: com.leyoumall.item.pojo.SpuDetail
     * @Author: wangJ1e
     * @Date: 2019-08-14
     **/
    @Override
    public SpuDetail querySpuDetailBySpuid(Long spuid) {
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuid);
        if(spuDetail == null){
            throw new LyException(ExceptionEnum.SPUDETAIL_NOT_FOUND);
        }
        return spuDetail;
    }
    /**
     * @MethodName: querySkuBySpuid
     * @Description: 根据spuid查询所有的sku的集合  TODO    此处多看，通过流转换集合 ,map
     * @Param: [spuid]
     * @Return: org.springframework.http.ResponseEntity<java.util.List<com.leyoumall.item.pojo.Sku>>
     * @Author: wangJ1e
     * @Date: 2019-08-14
     **/
    @Override
    public List<Sku> querySkuBySpuid(Long spuid) {
        //查询sku
        Sku sku = new Sku();
        sku.setSpuId(spuid);
        List<Sku> skus = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skus)){
            throw new LyException(ExceptionEnum.SKU_NOT_FOUND);
        }
        //查询库存  //循环查
        /*for (Sku s : skus) {
            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
            if(stock == null){
                throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
            }
            s.setStock(stock.getStock());
        }*/
        //批量查库存
        //先获取skuid的集合list
        List<Long> idList = skus.stream().map(Sku::getId).collect(Collectors.toList());
        loadStockInSku(skus,idList);
        return skus;
    }
    /**
     * @MethodName: updateGoods
     * @Description:
     * @Param: [spu]
     * @Return: void
     * @Author: wangJ1e
     * @Date: 2019-08-14
     **/
    @Override
    @Transactional
    public void updateGoods(Spu spu) {
        if(spu.getId() == null){
            throw new LyException(ExceptionEnum.SPUID_ERROR);
        }
        int count;
        Sku sku = new Sku();
        sku.setSpuId(spu.getId());
        //查询sku
        List<Sku> skus = skuMapper.select(sku);
        if(CollectionUtils.isEmpty(skus)){
            //删除sku
            count = skuMapper.delete(sku);
            if(count != skus.size()){
                throw new LyException(ExceptionEnum.SKU_DELETE_FAIL);
            }
            //删除stock
            List<Long> idList = skus.stream().map(Sku::getId).collect(Collectors.toList());
            this.stockMapper.deleteByIdList(idList);
            /*//或者
            // 删除以前库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", idList);
            this.stockMapper.deleteByExample(example);*/
        }
        //修改spu
        spu.setValid(null);
        spu.setSaleable(null);
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        //updateByPrimaryKeySelective   字段为null就不会更新，  根据主键跟新不为null的字段
        //updateByPrimaryKey  根据主键跟新所有字段
        count = spuMapper.updateByPrimaryKeySelective(spu);
        if(count != 1){
            throw new LyException(ExceptionEnum.UPDATE_SPU_FAIL);
        }
        //修改detail
        count = spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        if(count != 1){
            throw new LyException(ExceptionEnum.UPDATE_SPU_FAIL);
        }
        //新增sku和stock
        saveSkuAndStock(spu);
        //发送mq消息
        amqpTemplate.convertAndSend("item.upate",spu.getId());
    }

    @Override
    public Spu querySpuById(Long id) {
        //查spu
        Spu spu = spuMapper.selectByPrimaryKey(id);
        if (spu == null) {
            throw new LyException(ExceptionEnum.SPU_NOT_FOUND);
        }
        //查sku
        List<Sku> skus = querySkuBySpuid(id);
        spu.setSkus(skus);
        //查询详情
        SpuDetail spuDetail = querySpuDetailBySpuid(id);
        spu.setSpuDetail(spuDetail);
        return spu;
    }
    /**
     * @MethodName: querySkuByIds
     * @Description: 根据skuid集合查询sku得集合
     * @Param: [ids]
     * @Return: java.util.List<com.leyoumall.item.pojo.Sku>
     * @Author: wangJ1e
     * @Date: 2019-08-20
     **/
    @Override
    public List<Sku> querySkuByIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(skus)) {
            throw new LyException(ExceptionEnum.SKU_NOT_FOUND);
        }
        loadStockInSku(skus,ids);
        return skus;
    }

    private void loadStockInSku(List<Sku> skus,List<Long> idList) {
        //根据id集合查询所有库存对象集合
        List<Stock> stocks = stockMapper.selectByIdList(idList);
        if (CollectionUtils.isEmpty(skus)) {
            throw new LyException(ExceptionEnum.GOODS_STOCK_NOT_FOUND);
        }
        //如果不变成map，传统方法是 双重for循环 skus 和 stocks ，根据skuid相等来设置库存值
        //将库存对象集合stocks 变成map  key为skuid ,值为库存数量    此段代码多看  todo list转map
        Map<Long, Integer> map = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        //循环设置查询出来的skus集合的库存属性值   根据skuid相同获取值
        skus.forEach(s -> s.setStock(map.get(s.getId())));
    }

    private void loadCategoryAndBrandName(List<Spu> spus) {
        for (Spu spu : spus) {
            //根据id先查出分类对象的一个集合，每个对象对应一个分类名称
            //3个id对应3个名称，
            //.stream().map(Category::getName).collect(Collectors.toList())
            //通过Category的getName放方法转化成一个name为泛型的List
            //相当于每个对象只要name字段
            List<String> names = categoryService.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()))
                    .stream().map(Category::getName).collect(Collectors.toList());
            //将一个集合的每个对象变成字符串，并用/分割
            spu.setCname(StringUtils.join(names,"/"));
            /*/////////处理分类名称*/
            spu.setBname(brandService.queryByid(spu.getBrandId()).getName());
        }
    }
}
