package com.example.multidatasourcedemo.Dao;

import com.example.multidatasourcedemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: InfoMapper
 * @Auther: zhoucc
 * @Date: 2019/5/31 17:57
 * @Description:
 */

@Mapper
public interface UserDao {
    public User find();
}
