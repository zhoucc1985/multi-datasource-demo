package com.example.multidatasourcedemo.configure;

import com.example.multidatasourcedemo.Component.AcessInterceptor;
import com.example.multidatasourcedemo.Component.CurrentUserMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @ClassName: MvcConfig
 * @Auther: zhoucc
 * @Date: 2019/6/13 17:11
 * @Description:
 */

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private HandlerInterceptor acessInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CurrentUserMethodArgumentResolver());
    }

    /**
     * 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(acessInterceptor).addPathPatterns("/**").excludePathPatterns("/login", "/register");
    }

    /**
     * 这个方法是用来配置静态资源的，比如html，js，css，等等
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

        //把/swagger-ui.html这个路径映射到对应的目录META-INF/resources/
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    /**
     * 解决请求中的中文编码问题。将请求回应中charset=ISO-8859-1改为charset=UTF-8
     */
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
    }
}
