package com.leyoumall.item.service.impl;

import com.leyoumall.common.enums.ExceptionEnum;
import com.leyoumall.common.exception.LyException;
import com.leyoumall.item.mapper.SpecParamMapper;
import com.leyoumall.item.mapper.SpecificationMapper;
import com.leyoumall.item.pojo.SpecGroup;
import com.leyoumall.item.pojo.SpecParam;
import com.leyoumall.item.service.SpeccificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SpeccificayionServiceImpl
 * @Description:
 * @Author Administrator
 * @Date 2019-08-06
 * @Version V1.0
 **/
@Service
public class SpeccificationServiceImpl implements SpeccificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * @MethodName: querySpecGroupByCid
     * @Description: 根据分类id查询规格组  返回list
     * @Param: [cid]
     * @Return: java.util.List<com.leyoumall.item.pojo.SpecGroup>
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @Override
    public List<SpecGroup> querySpecGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> specGroups = specificationMapper.select(specGroup);
        if(CollectionUtils.isEmpty(specGroups)){
            throw new LyException(ExceptionEnum.SPECGROUP_NOT_FOUND);
        }
        return specGroups;
    }

    /**
     * @MethodName: addSpecGroup
     * @Description: 根据分类id新增规则组
     * @Param: [cid, name]
     * @Return: void
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @Override
    public void addSpecGroup(Long cid, String name) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        specGroup.setName(name);
        int i = specificationMapper.insert(specGroup);
        if(i != 1){
            throw new LyException(ExceptionEnum.SPECGROUP_SAVA_ERROR);
        }
    }

    /**
     * @MethodName: querySpecParamByGid
     * @Description: 根据规格组id查询规格参数
     * @Param: [gid]
     * @Return: java.util.List<com.leyoumall.item.pojo.SpecParam>
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @Override
    public List<SpecParam> querySpecParamList(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupid(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPECPARAM_NOT_FOUND);
        }
        return list;
    }

    /**
     * @MethodName: addSpecParam
     * @Description: 新增规格参数
     * @Param: [specParam]
     * @Return: void
     * @Author: Administrator
     * @Date: 2019-08-06
     **/
    @Override
    public void addSpecParam(SpecParam specParam) {
        int i = specParamMapper.insert(specParam);
        if(i != 1){
            throw new LyException(ExceptionEnum.SPECPARAM_SAVA_ERROR);
        }
    }
    /**
     * @MethodName: queryGroupByCid
     * @Description: TODO  商品详情页 查询规格参数
     * @Param: [cid]
     * @Return: java.util.List<com.leyoumall.item.pojo.SpecGroup>
     * @Author: wangJ1e
     * @Date: 2019-08-18
     **/
    @Override
    public List<SpecGroup> queryGroupByCid(Long cid) {
        //类下所有组
        List<SpecGroup> specGroups = querySpecGroupByCid(cid);
        //查询组内规格参数   分类下所有规格参数
        List<SpecParam> specParams = querySpecParamList(null, cid, null);
        //先把规格参数变成map，key是规格组id，值是组下所有参数
        Map<Long,List<SpecParam>> map = new HashMap<>();
        //循环specParams  放到map
        specParams.forEach(specParam -> {
            if (!map.containsKey(specParam.getCid())) {
                map.put(specParam.getCid(),new ArrayList<>());
            }
            //取出集合，加入此次specParam，---注意不要再put回map.
            map.get(specParam.getCid()).add(specParam);
        });
        //SpecParam到组
        specGroups.forEach(specGroup -> {
            specGroup.setParams(map.get(specGroup.getId()));
        });
        return specGroups;
    }
}
