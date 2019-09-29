package com.example.multidatasourcedemo.utils;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import java.util.ArrayList;
import java.util.List;

/**
 * 将json文件转换为对象
 * @author zhoucc
 * @date 2019/9/29 11:36
 */
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * 将json文件转列表对象
     * @param resource json文件类路径
     * @param clazz 类对象
     * @return 列表对象
     */
    public static <T> List<T> getListByJsonStr(ClassPathResource resource, Class<T> clazz) {
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return mapper.readValue(resource.getInputStream(), javaType);
        } catch (Exception e) {
            throw new RuntimeException("获取Json数据失败!" + e.getMessage(), e);
        }
    }

    /**
     * 将json文件转对象
     * @param resource json文件类路径
     * @param clazz 类对象
     * @return 列表对象
     */
    public static <T> T getObjectByJsonStr(ClassPathResource resource, Class<T> clazz) {
        try {
            return mapper.readValue(resource.getInputStream(), clazz);
        } catch (Exception e) {
            throw new RuntimeException("获取Json数据失败!" + e.getMessage(), e);
        }
    }
}
