package it.com.aop.dao;

import it.com.aop.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    @Select(value = "select * from tb_user where id = #{id}")
    User getById(Integer id);

    @SelectKey(statement = "select LAST_INSERT_ID()",keyColumn = "id",before = false,
            keyProperty = "id",resultType = int.class)
    @Insert(value = "insert into tb_user(user_name,age) values(#{userName}, #{age})")
    void insert(User user);

    @Update(value = "update tb_user set user_name=#{userName},age=#{age} where id=#{id}")
    void update(User user);

    @Delete(value = "delete from tb_user where id = #{id}")
    void delete(Integer id);

    @Select(value = "select count(id) from tb_user where user_name = #{userName}")
    int countByUserName(User user);
}
