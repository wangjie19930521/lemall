package com.leyoumall.item.service;

import com.leyoumall.item.pojo.Sku;
import com.leyoumall.item.pojo.Spu;
import com.leyoumall.item.pojo.SpuDetail;
import com.leyoumall.vo.PageResult;

import java.util.List;

/**
 * @ClassName GoodsService
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-07
 * @Version V1.0
 **/
public interface GoodsService {
    PageResult<Spu> querySpuByPage(Integer page, Integer rows, Boolean saleable, String key);

    void saveGoods(Spu spu);

    SpuDetail querySpuDetailBySpuid(Long spuid);

    List<Sku> querySkuBySpuid(Long spuid);

    void updateGoods(Spu spu);

    Spu querySpuById(Long id);

    List<Sku> querySkuByIds(List<Long> ids);

}
