package com.example.multidatasourcedemo.services;

import com.example.multidatasourcedemo.utils.JsonUtils;
import com.example.multidatasourcedemo.vo.HistoryInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 读取json文件到对象
 * @author zhoucc
 * @date 2019/9/29 11:35
 */

@Service
public class JsonService {

    @Value("static/data.json")
    private String resourceList;

    @Value("static/data1.json")
    private String resource;

    public List<HistoryInfo> getDataList() {
        List<HistoryInfo> historyInfos = JsonUtils.getListByJsonStr(new ClassPathResource(resourceList), HistoryInfo.class);
        System.out.println(historyInfos);
        return historyInfos;
    }

    public HistoryInfo getData() {
        HistoryInfo historyInfos = JsonUtils.getObjectByJsonStr(new ClassPathResource(resource), HistoryInfo.class);
        System.out.println(historyInfos);
        return historyInfos;
    }
}
