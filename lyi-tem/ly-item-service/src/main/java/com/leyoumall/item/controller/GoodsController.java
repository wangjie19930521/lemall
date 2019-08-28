package com.leyoumall.item.controller;

import com.leyoumall.item.pojo.Sku;
import com.leyoumall.item.pojo.Spu;
import com.leyoumall.item.pojo.SpuDetail;
import com.leyoumall.item.service.GoodsService;
import com.leyoumall.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName GoodsController
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-07
 * @Version V1.0
 **/
@RestController
@RequestMapping("spu")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * @MethodName: querySpuByPage
     * @Description:  查询SPU分页
     * @Param: [page, rows, saleable, key]
     * @Return: org.springframework.http.ResponseEntity<com.leyoumall.vo.PageResult<com.leyoumall.item.pojo.Spu>>
     * @Author: wangJ1e
     * @Date: 2019-08-07
     **/
    @GetMapping("/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) Boolean saleable,
            @RequestParam(value = "key",required = false) String key
    ){
        return ResponseEntity.ok(goodsService.querySpuByPage(page,rows,saleable,key));
    }

    /**
     * @MethodName: saveGoods
     * @Description: 保存商品
     * @Param: [spu]
     * @Return: org.springframework.http.ResponseEntity<java.lang.Void>
     * @Author: wangJ1e
     * @Date: 2019-08-13
     **/
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody Spu spu){
        goodsService.saveGoods(spu);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu){
        goodsService.updateGoods(spu);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    /**
     * @MethodName: querySpuDetailBySpuid
     * @Description: 根据spuid查询SpuDetail
     * @Param: [spuid]
     * @Return: com.leyoumall.item.pojo.SpuDetail
     * @Author: wangJ1e
     * @Date: 2019-08-14
     **/
    @GetMapping("/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuid(@PathVariable("id") Long spuid){
        return ResponseEntity.ok(goodsService.querySpuDetailBySpuid(spuid));
    }

    /**
     * @MethodName: querySkuBySpuid
     * @Description: 根据spuid查询所有的sku的集合
     * @Param: [spuid]
     * @Return: org.springframework.http.ResponseEntity<java.util.List<com.leyoumall.item.pojo.Sku>>
     * @Author: wangJ1e
     * @Date: 2019-08-14
     **/
    @GetMapping("/list")
    public ResponseEntity<List<Sku>> querySkuBySpuid(@RequestParam("id") Long spuid){
        List<Sku> list = goodsService.querySkuBySpuid(spuid);
       return null;
    }

    /**
     * @MethodName: querySpuById
     * @Description:
     * @Param: [id]
     * @Return: org.springframework.http.ResponseEntity<com.leyoumall.item.pojo.Spu>
     * @Author: wangJ1e
     * @Date: 2019-08-18
     **/
    @GetMapping("/spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.querySpuById(id));

    }

    @GetMapping("/sku/list/{ids}")
    public ResponseEntity<List<Sku>> querySkuByIds(@RequestParam("ids") List<Long> ids){
        List<Sku> list = goodsService.querySkuByIds(ids);
        return null;
    }
}
