package com.leyoumall.search;

import com.leyoumall.search.pojo.Goods;
import com.leyoumall.search.pojo.SearchRequest;
import com.leyoumall.search.service.SearchService;
import com.leyoumall.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SearchController
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-16
 * @Version V1.0
 **/
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("/page")
    public ResponseEntity<PageResult<List<Goods>>> search(@RequestBody SearchRequest searchRequest){
        PageResult<Goods> pageResult = searchService.search(searchRequest);
        return null;
    }
}
