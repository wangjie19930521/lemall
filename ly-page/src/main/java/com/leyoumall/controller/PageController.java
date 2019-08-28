package com.leyoumall.controller;

import com.leyoumall.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.thymeleaf.TemplateEngine;

import java.util.Map;

/**
 * @ClassName PageController
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-18
 * @Version V1.0
 **/
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    /**
     * @MethodName: toItemPage
     * @Description: TODO 详情页所有展示数据
     * @Param: [spuId, model]
     * @Return: java.lang.String
     * @Author: wangJ1e
     * @Date: 2019-08-18
     **/
    @GetMapping("/item/{id}.html")
    public String toItemPage(@PathVariable("id") Long spuId, Model model){
        Map<String, Object> data = pageService.loadData(spuId);
        model.addAllAttributes(data);
        return "item";
    }
}
