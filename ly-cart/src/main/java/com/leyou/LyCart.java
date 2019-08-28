package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName LyCart
 * @Description:
 * @Author wangJ1e
 * @Date 2019-08-20
 * @Version V1.0
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class LyCart {
    public static void main(String[] args) {
        SpringApplication.run(LyCart.class,args);
    }
}
