package com.whl.messagesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wanghanlin
 */
@MapperScan("com.whl.messagesystem.dao")
@SpringBootApplication
public class WsMessageSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsMessageSystemApplication.class, args);
    }

}
