package com.leyoumall.config;

import com.leyoumall.common.utils.IdWorker;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName IdWorkConfig
 * @Description:
 * @EnableConfigurationProperties(IdWorkerProperties.class)  容器将该实例生成传入 idWorker
 * @Configuration   标记该类为配置类，@Bean  将此实例在容器生成，其他地方可以拿到该对象  直接注入就行了
 * @Author wangJ1e
 * @Date 2019-08-24
 * @Version V1.0
 **/
@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkConfig {

    @Bean
    public IdWorker idWorker(IdWorkerProperties prop){
        return new IdWorker(prop.getWorkerId(),prop.getDataCenterId());
    }
}
