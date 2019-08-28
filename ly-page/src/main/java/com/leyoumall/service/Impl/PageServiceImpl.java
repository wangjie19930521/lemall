package com.leyoumall.service.Impl;

import com.leyoumall.client.BrandClient;
import com.leyoumall.client.CategoryClient;
import com.leyoumall.client.GoodsClient;
import com.leyoumall.client.SpeccificationClient;
import com.leyoumall.item.pojo.*;
import com.leyoumall.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PageServiceImpl
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
@Slf4j
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private SpeccificationClient speccificationClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public Map<String, Object> loadData(Long spuId) {
        //查询spu
        Spu spu = goodsClient.querySpuById(spuId);
        //skus
        List<Sku> skus = goodsClient.querySkuBySpuid(spuId);
        //查询spu详情
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuid(spuId);
        //查询品牌
        Brand brand = brandClient.queryBrandByid(spu.getBrandId());
        //分类
        List<Category> categories = categoryClient.queryCategoryByIds(
                Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        //规格参数
        List<SpecGroup> specGroups = speccificationClient.queryGroupByCid(spu.getCid3());
        Map<String, Object> map = new HashMap<>();
        map.put("spu",spu);
        map.put("skus",skus);
        map.put("detail",spuDetail);
        map.put("brand",brand);
        map.put("categories",categories);
        map.put("specs",specGroups);
        return map;
    }

    /**
     * @MethodName: createHtml
     * @Description: TODO  生成静态页
     * @Param: [spuid]
     * @Return: void
     * @Author: wangJ1e
     * @Date: 2019-08-18
     **/
    @Override
    public void createHtml(Long spuid) {
        Context context = new Context();
        context.setVariables(loadData(spuid));
        File file = new File("E:\\百度云下载\\资料\\静态原型",spuid+".html");
        if (file.exists()) {
            file.delete();
        }
        try (PrintWriter writer = new PrintWriter(file,"UTF-8")){
            templateEngine.process("item",context,writer);
        } catch (Exception e) {
            log.error("生成静态页错误");
        }
    }

    @Override
    public void deleteHtml(Long spuid) {
        File file = new File("E:\\百度云下载\\资料\\静态原型",spuid+".html");
        if (file.exists()) {
            file.delete();
        }
    }
}
