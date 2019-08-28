package com.leyoumall.item.controller;

import com.leyoumall.item.pojo.SpecGroup;
import com.leyoumall.item.pojo.SpecParam;
import com.leyoumall.item.service.SpeccificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName SpeccificayionController
 * @Description:
 * @Author Administrator
 * @Date 2019-08-06
 * @Version V1.0
 **/
@RestController
@RequestMapping("spec")
public class SpeccificationController {

    @Autowired
    private SpeccificationService speccificationService;

    /**
     * @MethodName: querySpecGroupByCid
     * @Description: 根据分类id查询规格组  返回list
     * @Return: org.springframework.http.ResponseEntity<java.util.List<com.leyoumall.item.pojo.SpecGroup>>
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @RequestMapping("/group/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroupByCid(@PathVariable("cid") Long cid){
        return ResponseEntity.ok(speccificationService.querySpecGroupByCid(cid));
    }

    /**
     * @MethodName: addSpecGroup
     * @Description: 根据分类id新增规则组
     * @Param: [cid, name]
     * @Return: org.springframework.http.ResponseEntity<java.lang.Void>
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @PostMapping("/group")
    public ResponseEntity<Void> addSpecGroup(@RequestParam("cid") Long cid,
                                       @RequestParam("name") String name){
        speccificationService.addSpecGroup(cid,name);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * @MethodName: querySpecParamByGid
     * @Description: 根据规格组id查询规格参数
     * @Param: [gid]   cid   searching
     * @Return: org.springframework.http.ResponseEntity<java.util.List<com.leyoumall.item.pojo.SpecParam>>
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>> querySpecParamList(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value="cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching){
        return ResponseEntity.ok(speccificationService.querySpecParamList(gid,cid,searching));
    }

    /**
     * @MethodName: addSpecParam
     * @Description: 新增规格参数
     * @Param: [specParam]
     * @Return: org.springframework.http.ResponseEntity<java.lang.Void>
     * @Author: wangJ1e
     * @Date: 2019-08-06
     **/
    @PostMapping("/params")
    public ResponseEntity<Void> addSpecParam(SpecParam specParam){
        speccificationService.addSpecParam(specParam);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> queryGroupByCid(@RequestParam("cid") Long cid){
        return ResponseEntity.ok(speccificationService.queryGroupByCid(cid));
    }
}
