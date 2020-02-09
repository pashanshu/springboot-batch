package com.zc;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by zc on 2019/11/13
 */
@SpringBootApplication
@MapperScan("com.zc.dao")
public class SpringBatchApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringBatchApplication.class);

    public static void main(String[] args) {
//        SpringApplication.run(SpringBatchApplication.class, args);
        ConfigurableApplicationContext cac = SpringApplication.run(SpringBatchApplication.class, args);
        String[] beans = cac.getBeanDefinitionNames();
        for(String str:beans){
            log.info("the bean is {}",str);
        }
    }
}
