package com.example.multidatasourcedemo.Dao.second;

import com.example.multidatasourcedemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: InfoMapper
 * @Auther: zhoucc
 * @Date: 2019/5/31 17:57
 * @Description:
 */

@Mapper
public interface UserSecondDao {
    User find();

    List<User> list();

    int insert(User user);
}