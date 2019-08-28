package com.leyoumall.search.service;

import com.leyoumall.item.pojo.Spu;
import com.leyoumall.search.pojo.Goods;
import com.leyoumall.search.pojo.SearchRequest;
import com.leyoumall.vo.PageResult;

import java.util.List;

public interface SearchService {
    Goods buildGoods(Spu spu);

    PageResult<Goods> search(SearchRequest searchRequest);

    void createOrUpdateIndex(Long spuid);

    void deleteIndex(Long spuid);

}
