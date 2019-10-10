package com.example.multidatasourcedemo.services.impl;

import com.example.multidatasourcedemo.dao.second.TtlProductInfoMapper;
import com.example.multidatasourcedemo.eumn.ExcelFormatEumn;
import com.example.multidatasourcedemo.pojo.ExcelHeaderInfo;
import com.example.multidatasourcedemo.pojo.TtlProductInfoPo;
import com.example.multidatasourcedemo.services.TtlProductInfoService;
import com.example.multidatasourcedemo.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 商品Service
 * @author zhoucc
 * @date 2019/10/9 16:36
 */

@Service
@Slf4j
public class TtlProductInfoServiceImpl implements TtlProductInfoService {

    /**
     * 每个线程导出记录最大行数
     */
    private static final int THREAD_MAX_ROW = 20000;

    @Resource
    private TtlProductInfoMapper mapper;

    @Override
    public List<TtlProductInfoPo> listProduct(Map<String, Object> map) {
        return this.mapper.listProduct(map);
    }

    @Override
    public void export(HttpServletResponse response, HttpServletRequest request, String fileName) {
        // 待导出数据
        List<TtlProductInfoPo> productInfoPos = this.multiThreadListProduct();
        ExcelUtils excelUtils = new ExcelUtils(productInfoPos, getHeaderInfo(), getFormatInfo());
        excelUtils.sendHttpResponse(response, request, fileName, excelUtils.getWorkbook());
    }

    /**
     * 每个线程导出记录最大行数
     */
    private List<ExcelHeaderInfo> getHeaderInfo() {
        // 重点：这里Arrays.asList的参数为数组且数组里面为对象，则List大小就是数组的大小。如果数组里面为基本类型，则将数组本身当成一个对象，则List只有一个元素.
        return Arrays.asList(
                new ExcelHeaderInfo(0, 0, 0, 0, "id"),
                new ExcelHeaderInfo(0, 0, 1, 1, "商品名称"),

//                new ExcelHeaderInfo(0, 0, 2, 3, "分类"),
                new ExcelHeaderInfo(0, 0, 2, 2, "类型ID"),
                new ExcelHeaderInfo(0, 0, 3, 3, "分类名称"),

//                new ExcelHeaderInfo(0, 0, 4, 5, "品牌"),
                new ExcelHeaderInfo(0, 0, 4, 4, "品牌ID"),
                new ExcelHeaderInfo(0, 0, 5, 5, "品牌名称"),

//                new ExcelHeaderInfo(0, 0, 6, 7, "商店"),
                new ExcelHeaderInfo(0, 0, 6, 6, "商店ID"),
                new ExcelHeaderInfo(0, 0, 7, 7, "商店名称"),

                new ExcelHeaderInfo(0, 0, 8, 8, "价格"),
                new ExcelHeaderInfo(0, 0, 9, 9, "库存"),
                new ExcelHeaderInfo(0, 0, 10, 10, "销量"),
                new ExcelHeaderInfo(0, 0, 11, 11, "插入时间"),
                new ExcelHeaderInfo(0, 0, 12, 12, "更新时间"),
                new ExcelHeaderInfo(0, 0, 13, 13, "记录是否已经删除")
        );
    }

    /**
     * 获取格式化信息
     */
    private Map<String, ExcelFormatEumn> getFormatInfo() {
        Map<String, ExcelFormatEumn> format = new HashMap<>();
        format.put("id", ExcelFormatEumn.FORMAT_INTEGER);
        format.put("categoryId", ExcelFormatEumn.FORMAT_INTEGER);
        format.put("branchId", ExcelFormatEumn.FORMAT_INTEGER);
        format.put("shopId", ExcelFormatEumn.FORMAT_INTEGER);
        format.put("price", ExcelFormatEumn.FORMAT_DOUBLE);
        format.put("stock", ExcelFormatEumn.FORMAT_INTEGER);
        format.put("salesNum", ExcelFormatEumn.FORMAT_INTEGER);
        format.put("isDel", ExcelFormatEumn.FORMAT_INTEGER);
        return format;
    }

    /**
     * 多线程查询报表
     */
    private List<TtlProductInfoPo> multiThreadListProduct() {
        List<FutureTask<List<TtlProductInfoPo>>> tasks = new ArrayList<>();
        List<TtlProductInfoPo> productInfoPos = new ArrayList<>();

        int totalNum = 500000;
        int loopNum = new Double(Math.ceil((double) totalNum / THREAD_MAX_ROW)).intValue();
        log.info("多线程查询，总数：{},开启线程数：{}", totalNum, loopNum);
        long start = System.currentTimeMillis();

        executeTask(tasks, loopNum, totalNum);

        for (FutureTask<List<TtlProductInfoPo>> task : tasks) {
            try {
                productInfoPos.addAll(task.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("查询结束，耗时:{}", System.currentTimeMillis() - start);
        return productInfoPos;
    }

    /**
     * 执行查询任务
     */
    private void executeTask(List<FutureTask<List<TtlProductInfoPo>>> tasks, int loopNum, int total) {
        for (int i = 0; i < loopNum; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("offset", i * THREAD_MAX_ROW);
            if (i == loopNum - 1) {
                map.put("limit", total - THREAD_MAX_ROW * i);
            } else {
                map.put("limit", THREAD_MAX_ROW);
            }
            FutureTask<List<TtlProductInfoPo>> task = new FutureTask<>(new listThread(map));
            log.info("开始查询第{}条开始的{}条记录", i * THREAD_MAX_ROW, THREAD_MAX_ROW);
            new Thread(task).start();
            // 将任务添加到tasks列表中
            tasks.add(task);
        }
    }

    private class listThread implements Callable<List<TtlProductInfoPo>> {

        private Map<String, Object> map;

        private listThread(Map<String, Object> map) {
            this.map = map;
        }

        @Override
        public List<TtlProductInfoPo> call() {
            return listProduct(map);
        }
    }

}
