package com.leyoumall.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName IdWorkerProperties
 * @Description: TODO
 * @Author wangJ1e
 * @Date 2019-08-24
 * @Version V1.0
 **/
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {

    private Long workerId;  //机器ID

    private Long dataCenterId;  //序列号
}
