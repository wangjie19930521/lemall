package com.leyoumall.service;

import java.util.Map;

public interface PageService {

    Map<String,Object> loadData(Long spuId);

    void createHtml(Long spuid);

    void deleteHtml(Long spuid);
}
