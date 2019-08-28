package com.leyoumall.item.service;


import com.leyoumall.item.pojo.SpecGroup;
import com.leyoumall.item.pojo.SpecParam;

import java.util.List;

public interface SpeccificationService {


    List<SpecGroup> querySpecGroupByCid(Long cid);

    void addSpecGroup(Long cid, String name);

    List<SpecParam> querySpecParamList(Long gid,Long cid,Boolean searching);

    void addSpecParam(SpecParam specParam);

    List<SpecGroup> queryGroupByCid(Long cid);
}
