package com.leyoumall.search.pojo;

import com.leyoumall.item.pojo.Brand;
import com.leyoumall.item.pojo.Category;
import com.leyoumall.vo.PageResult;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResult extends PageResult<Goods> {

    private List<Category> categories;

    private List<Brand> brands;

    private List<Map<String, Object>> specs; // 规格参数过滤条件

    public SearchResult() {
    }

/*    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
    }*/

    public SearchResult(Long total, Long totalPage, List<Goods> items, List<Category> categories, List<Brand> brands, List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
