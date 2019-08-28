package com.leyoumall.search.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.leyoumall.common.utils.JsonUtils;
import com.leyoumall.item.pojo.*;
import com.leyoumall.search.client.BrandClient;
import com.leyoumall.search.client.CategoryClient;
import com.leyoumall.search.client.GoodsClient;
import com.leyoumall.search.client.SpeccificationClient;
import com.leyoumall.search.pojo.Goods;
import com.leyoumall.search.pojo.SearchRequest;
import com.leyoumall.search.pojo.SearchResult;
import com.leyoumall.search.repository.GoodsRepository;
import com.leyoumall.search.service.SearchService;
import com.leyoumall.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SearchService
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-15
 * @Version V1.0
 **/
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpeccificationClient speccificationClient;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * @MethodName: buildGoods
     * @Description: TODO 构建Goods存入索引库
     * @Param: [spu]
     * @Return: com.leyoumall.search.pojo.Goods
     * @Author: wangJ1e
     * @Date: 2019-08-15
     **/
    public Goods buildGoods(Spu spu){
        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
        List<String> names = categories.stream().map(Category::getName).collect(Collectors.toList());
        //查询品牌
        Brand brand = brandClient.queryBrandByid(spu.getBrandId());
        //搜索字段all
        String all = spu.getTitle() + StringUtils.join(names," ") + brand.getName();
        //查询sku
        List<Sku> skusList = goodsClient.querySkuBySpuid(spu.getId());
        /*//得到价格set    流也代表一次循环，影响效率，直接在下面循环时就处理得到价格。
        //Set<Long> priceSet = skusList.stream().map(Sku::getPrice).collect(Collectors.toSet());*/
        //对sku进行处理   sku字段太多，不需要全部放入索引库   得到新的skus的集合
        List<Map<String,Object>> skus = new ArrayList<>();
        Set<Long> priceSet = new HashSet();
        for (Sku sku : skusList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id",sku.getId());
            map.put("title",sku.getTitle());
            map.put("price",sku.getPrice());
            //substringBefore   截取第一个，之前的
            map.put("image",StringUtils.substringBefore(sku.getImages(),","));
            skus.add(map);
            //处理价格
            priceSet.add(sku.getPrice());
        }
        //---------------
        //查询规格参数 模板(通用和特殊参数都在里面)  --key   可以用于搜索的 根据分类的那套参数   组id为空，  分类id来查询此类所有的参数
        List<SpecParam> specParams = speccificationClient.querySpecParamList(null, spu.getCid3(), true);
        //查询商品详情  --value
        SpuDetail spuDetail = goodsClient.querySpuDetailBySpuid(spu.getId());
        //获取通用规格参数值   查出来是key-value的json串     转为map
        Map<String, String> generricSpecMap = JsonUtils.parseMap(spuDetail.getGenerricSpec(), String.class, String.class);
        //获取特有规格参数值
          /* //-------调用例子   json  里面是  map 里面是 List，*/
        Map<String, List<String>> specialSpecMap = JsonUtils.nativeRead(spuDetail.getSpecialSpec(), new TypeReference<Map<String, List<String>>>() {
        });
        //规格参数   specMap  用于存放规格参数的对应值  例如：cpu:2.5HZ
        Map<String,Object> specMap = new HashMap<>();
        for (SpecParam specParam : specParams) {
            //规格名称
            String key = specParam.getName();
            Object value = "";
            //判断是否通用规格参数
            if(specParam.getGeneric()){
                //存模板参数 存值  以id关联
                value = generricSpecMap.get(specParam.getId());
             //判断value是否数值,前台搜索的时候如果是数值分段的搜索,现在直接处理成分段存入索引库，搜索是就可以匹配查询，不用分段搜索
                if (specParam.getNumeric()) {
                    //处理成段
                    value = chooseSegment(value.toString(), specParam);
                }
            }else{
                value = specialSpecMap.get(specParam.getId());
            }
            specMap.put(key,value);
        }
        Goods goods = new Goods();
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateTime(spu.getCreateTime());
        goods.setId(spu.getId());
        goods.setAll(all);
        goods.setPrice(priceSet);
        goods.setSkus(JsonUtils.serialize(skus));
        goods.setSpecs(specMap);
        goods.setSubTitle(spu.getSubTitle());
        return goods;
    }
    /**
     * @MethodName: search
     * @Description: TODO 查询   页面搜索  展示商品  搜索的过滤条件
     * @Param: [searchRequest]
     * @Return: com.leyoumall.vo.PageResult<com.leyoumall.search.pojo.Goods>
     * @Author: wangJ1e
     * @Date: 2019-08-17
     **/

    @Override
    public PageResult<Goods> search(SearchRequest searchRequest) {
        Integer page = searchRequest.getPage() - 1;//elasticsearch页数从0开始
        Integer size = searchRequest.getSize();
        //创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //分页
        queryBuilder.withPageable(PageRequest.of(page,size));
        //添加条件
        QueryBuilder basicQuery = buildBasicQuery(searchRequest);
        queryBuilder.withQuery(basicQuery);
        //过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));//结果过滤
        //聚合分类和品牌
        String categoryAgg = "categoryAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAgg).field("cid3"));
        String brandAgg = "brandAgg";
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAgg).field("brandId"));
        //查询    聚合就要用elasticsearchTemplate，
        AggregatedPage<Goods> goods = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);
        //解析分页结果
        long total = goods.getTotalElements();
        Long totlePage = Long.valueOf(goods.getTotalPages());
        List<Goods> goodsList = goods.getContent();
        //解析聚合结果   解析聚合结果(页面查询时候的 Aggregations)
        Aggregations aggs = goods.getAggregations();
        //分别得到  聚合
        List<Category> categorys = parseCategoryAgg(aggs.get(categoryAgg));// 值是什么类型就是什么terms  LongTerms 中才有getBuckets
        List<Brand> brands = parseBrandAgg(aggs.get(brandAgg));
        //完成规格参数聚合
        List<Map<String,Object>> specs = null;
        //分类为1 才聚合规格参数去前台显示  如果分类不为1 ，参数就很杂
        if (categorys != null && categorys.size() == 1) {
            specs = buildSpecifitionAgg(categorys.get(0).getId(),basicQuery);
        }
        return new SearchResult(total,totlePage,goodsList,categorys,brands,specs);
    }

    @Override
    public void createOrUpdateIndex(Long spuid) {
        //查询spu
        Spu spu = goodsClient.querySpuById(spuid);
        //构建good
        Goods goods = buildGoods(spu);

        goodsRepository.save(goods);
    }

    @Override
    public void deleteIndex(Long spuid) {
        goodsRepository.deleteById(spuid);
    }

    private QueryBuilder buildBasicQuery(SearchRequest searchRequest) {
        //创建布尔查询
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all",searchRequest.getKey()));
        //过滤条件
        Map<String, String> map = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            //处理key   如果前台点击过滤不是分类和品牌
            if (!"cid3".equals(key) && !"brandid".equals(key)) {
                key = "specs." + key +".keyword";
            }
            queryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }
        return queryBuilder;
    }

    private List<Map<String, Object>> buildSpecifitionAgg(Long cid, QueryBuilder basicQuery) {
        List<Map<String, Object>> spec = new ArrayList<>();
        //查询需要这种分类聚合的规格参数
        List<SpecParam> params = speccificationClient.querySpecParamList(null, cid, true);
        //聚合
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //调加查询条件   在查询条件的结果基础上进行聚合
        queryBuilder.withQuery(basicQuery);
        //params   各类规格参数    对各类进行聚合 展示
        for (SpecParam param : params) {
            String name = param.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        }//增强for循环
        /*params.forEach(specParam -> {
            String name = specParam.getName();
            queryBuilder.addAggregation(AggregationBuilders.terms(name).field("specs." + name + ".keyword"));
        });*/
        AggregatedPage<Goods> goods = elasticsearchTemplate.queryForPage(queryBuilder.build(), Goods.class);
        //获取全部聚合桶   params   很多种参数规格  ，terms  一种规格下面有很多种值得聚合    options值得集合
        Aggregations aggs = goods.getAggregations();
        for (SpecParam param : params) {
            String name = param.getName();
            StringTerms terms = aggs.get(name);
            List<String> options = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            Map<String, Object> map = new HashMap<>();
            map.put("k",name);
            map.put("options",options);
            spec.add(map);
        }
        return spec;
    }

    private List<Brand> parseBrandAgg(LongTerms terms) {
        try {
            List<Long> ids = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Brand> brands = brandClient.queryBrandByIds(ids);
            return brands;
        }catch (Exception e){
            log.error("搜索服务:查询品牌异常" + e);
            return null;
        }

    }
    private List<Category> parseCategoryAgg(LongTerms terms) {
        try {
           /* //terms.getBuckets().stream().map(LongTerms.Bucket :: getKeyAsNumber).collect(Collectors.toList());*/
            List<Long> ids = terms.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue())
                    .collect(Collectors.toList());
            List<Category> categories = categoryClient.queryCategoryByIds(ids);
            return categories;
        }catch (Exception e){
            log.error("搜索服务:查询分类异常" + e);
            return null;
        }
    }

    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if (segs.length == 2) {
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if (val >= begin && val < end) {
                if (segs.length == 1) {
                    result = segs[0] + p.getUnit() + "以上";
                } else if (begin == 0) {
                    result = segs[1] + p.getUnit() + "以下";
                } else {
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }
}
