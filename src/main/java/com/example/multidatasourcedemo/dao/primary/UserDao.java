package com.example.multidatasourcedemo.dao.primary;

import com.example.multidatasourcedemo.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 通过id集合批量查询
     * @param ids id集合
     * @return 用户对象集合
     */
    List<User> findSelectiveByIds(@Param("ids") List ids);

    /**
     * 通过id集合批量删除
     * @param ids id集合
     * @return 操作成功个数
     */
    int deleteSelectiveByIds(@Param("ids") List ids);

    /**
     * 批量更新用户对象
     * @param users 更新的对象
     * @param ids 需要更新的id集合
     * @return 操作成功的个数
     */
    int updateSelective(@Param("users") List users, @Param("ids") List<Long> ids);

    /**
     * 批量插入用户对象
     * @param users 被插入的用户对象集合
     * @return 成功操作个数
     */
    int insertSelective(@Param("users") List users);
}