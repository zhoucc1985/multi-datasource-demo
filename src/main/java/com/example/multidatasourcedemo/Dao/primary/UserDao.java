package com.example.multidatasourcedemo.Dao.primary;

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
public interface UserDao {
    public User find();

    public List<User> list();

    public int insert(User user);
}