package com.leyoumall.client;

import com.leyoumall.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName UserClient
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-19
 * @Version V1.0
 **/
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
