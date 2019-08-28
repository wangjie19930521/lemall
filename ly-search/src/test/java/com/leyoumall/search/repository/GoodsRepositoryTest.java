package com.leyoumall.search.repository;

import com.leyoumall.item.pojo.Spu;
import com.leyoumall.search.client.GoodsClient;
import com.leyoumall.search.pojo.Goods;
import com.leyoumall.search.service.SearchService;
import com.leyoumall.vo.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsRepositoryTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SearchService searchService;

    /*创建索引库*/
    @Test
    private void createIndex(){
        elasticsearchTemplate.createIndex(Goods.class);
        elasticsearchTemplate.putMapping(Goods.class);
    }


    @Test
    private void insertGoods(){
        int page = 1;
        int row = 100;
        int size = 0;
        do {
        PageResult<Spu> pageResult = goodsClient.querySpuByPage(page, row, true, null);
        List<Spu> items = pageResult.getItems();
        if (CollectionUtils.isEmpty(items)) {
            break;
        }
        //构建Goods
        List<Goods> goods = items.stream().map(searchService::buildGoods).collect(Collectors.toList());
        //存入索引库
        goodsRepository.saveAll(goods);
        page++;
        size = items.size();
        }while (size == 100);
    }


}
