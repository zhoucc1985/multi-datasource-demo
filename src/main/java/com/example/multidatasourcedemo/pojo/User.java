package com.example.multidatasourcedemo.pojo;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName: User
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:35
 * @Description:
 */

@Data
@Builder
public class User {

    private long id;

    private String userName;
}
