package com.leyoumall.item.api;


import com.leyoumall.item.pojo.Sku;
import com.leyoumall.item.pojo.Spu;
import com.leyoumall.item.pojo.SpuDetail;
import com.leyoumall.vo.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {
    @GetMapping("/spu/page")
    PageResult<Spu> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "key",required = false) String key
    );
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailBySpuid(@PathVariable("id") Long spuid);

    @GetMapping("/spu/list")
    List<Sku> querySkuBySpuid(@RequestParam("id") Long spuid);

    @GetMapping("/spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);
    @GetMapping("/sku/list")
    List<Sku> querySkuByIds(@RequestParam("ids") List<Long> ids);
}
