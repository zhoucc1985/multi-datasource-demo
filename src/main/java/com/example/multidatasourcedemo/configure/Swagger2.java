package com.example.multidatasourcedemo.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName: Swagger2
 * @Auther: zhoucc
 * @Date: 2019/6/14 11:38
 * @Description:
 */


@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.multidatasourcedemo.controller"))
                .paths(PathSelectors.any())
                .build();

    }

    /**
     * 构建api文档的详细信息函数
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                //描述
                .description("测试")
                .contact("周承超")
                .version("1.0")
                .build();
    }
}
