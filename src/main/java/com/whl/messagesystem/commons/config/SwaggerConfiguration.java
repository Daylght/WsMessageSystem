package com.whl.messagesystem.commons.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author whl
 * @date 2021/12/29 20:57
 */
public class SwaggerConfiguration {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.whl.messagesystem.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("王瀚林", "http://47.108.139.22:8888", "wangkeliu@hotmail.com");
        return new ApiInfoBuilder()
                .title("WsMS接口文档")
                .description("提供简单的分组消息服务")
                .contact(contact)
                .version("1.0")
                .build();
    }
}
