package com.example.multidatasourcedemo.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: User
 * @Auther: zhoucc
 * @Date: 2019/6/3 11:35
 * @Description:
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private long id;

    private String userName;

    public User(String userName) {
        this.userName = userName;
    }
}
