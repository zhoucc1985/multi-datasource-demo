package com.example.multidatasourcedemo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 纠错历史信息对象
 * @author zhoucc
 * @date 2019/9/27 15:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryInfo {

    private Long id;

    private String a;

    private String b;

    private String c;

    private String d;

    private String e;

    private String f;

    private List<HistoryInfo> children;
}
