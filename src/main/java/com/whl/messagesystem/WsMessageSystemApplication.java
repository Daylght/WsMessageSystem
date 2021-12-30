package com.whl.messagesystem;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author wanghanlin
 */
@EnableSwagger2
@EnableSwaggerBootstrapUI
@MapperScan("com.whl.messagesystem.dao")
@SpringBootApplication
public class WsMessageSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(WsMessageSystemApplication.class, args);
    }

}
