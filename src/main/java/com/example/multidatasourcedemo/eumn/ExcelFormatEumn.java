package com.example.multidatasourcedemo.eumn;

/**
 * Excel字段格式
 * @author zhoucc
 * @date 2019/10/9 16:36
 */
public enum ExcelFormatEumn {

    FORMAT_INTEGER("INTEGER"),
    FORMAT_DOUBLE("DOUBLE"),
    FORMAT_PERCENT("PERCENT"),
    FORMAT_DATE("DATE");

    private String value;

    ExcelFormatEumn(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
