package com.leyoumall.client;

import com.leyoumall.item.api.GoodsApi;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("item-service")
public interface GoodsClient extends GoodsApi {
}
