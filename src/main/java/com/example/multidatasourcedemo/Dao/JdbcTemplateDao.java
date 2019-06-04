package com.example.multidatasourcedemo.Dao;

import com.example.multidatasourcedemo.pojo.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @ClassName: JdbcTemplateDao
 * @Auther: zhoucc
 * @Date: 2019/6/4 15:01
 * @Description: JdbcTemplate 操作数据库
 */

@Slf4j
@Repository
public class JdbcTemplateDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 功能描述: 主键自增
     * @auther: zhoucc
     * @date: 2019/6/4 16:40
     */
    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    private NamedParameterJdbcTemplate parameterJdbcTemplate;

    /**
     * 功能描述: 通过JdbcTemplate查询所有记录
     *
     * @auther: zhoucc
     * @date: 2019/6/4 15:30
     */
    public void listData() {
        log.info("Count: {}",
                jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class));
        List<String> userNameList = jdbcTemplate.queryForList("SELECT user_name FROM users", String.class);
        userNameList.forEach(element->log.info("user_name: {}", element));
    }

    /**
     * 功能描述: JdbcTemplate 通过id查询对应记录
     *
     * @param:
     * @auther: zhoucc
     * @date: 2019/6/4 15:31
     */
    public User selectById(int id) {
        User user = User.builder().build();
        String sql = "SELECT * FROM users where id = ?";
        jdbcTemplate.query(sql, new Object[]{id}, (RowCallbackHandler) rs -> {
            user.setId(rs.getInt(1));
            user.setUserName(rs.getString(2));
        });
        return user;
    }

    /**
     * 功能描述: JdbcTemplate 新增数据并且主键自增
     *
     * @param: User 新增数据对象
     * @auther: zhoucc
     * @date: 2019/6/4 16:01
     */
    public int insertData(User user) {
        String sql = "INSERT INTO users(user_name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator preparedStatementCreator = con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUserName());
            return ps;
        };
        int num = jdbcTemplate.update(preparedStatementCreator, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return num;
    }

    /**
     * 功能描述: JdbcTemplate 新增数据另一种方法,并且主键自增
     *
     * @auther: zhoucc
     * @date: 2019/6/4 16:41
     */
    public void insertDataNew(@NonNull User user) {
        SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
        long id = simpleJdbcInsert.executeAndReturnKey(sps).longValue();
        user.setId(id);
    }

    /**
     * 功能描述: 更新
     *
     * @param: 更新对象
     * @auther: zhoucc
     * @date: 2019/6/4 16:56
     */
    public int update(@NonNull User user) {
        return jdbcTemplate.update("UPDATE users SET user_name = ? WHERE id = ?", new Object[]{user.getUserName(),user.getId(), });
    }

    /**
     * 功能描述: 根据id删除记录
     *
     * @auther: zhoucc
     * @date: 2019/6/4 17:09
     */
    public int deleteById(@NonNull int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", new Object[]{id});
    }

    /**
     * 功能描述: 批量处理，减少网络来回次数
     *
     * @auther: zhoucc
     * @date: 2019/6/4 17:26
     */
    public void batchInsert() {
        jdbcTemplate.batchUpdate("INSERT INTO users (user_name) VALUES (?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, "zhoucc-" + i);
            }

            @Override
            public int getBatchSize() {
                return 2;
            }
        });
    }

    /**
     * 功能描述: springboot 批量新增
     *
     * @param: 新增对象列表
     * @auther: zhoucc
     * @date: 2019/6/4 17:42
     */
    public void batchNewInsert(List<User> userList) {
        parameterJdbcTemplate
                .batchUpdate("INSERT INTO users (user_name) VALUES (:userName)",
                        SqlParameterSourceUtils.createBatch(userList));
    }
}
