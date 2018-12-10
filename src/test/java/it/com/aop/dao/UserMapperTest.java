package it.com.aop.dao;

import it.com.aop.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void insert() {
        User user = new User();
        user.setUserName(UUID.randomUUID().toString().substring(0,10));
        user.setAge(20);
        userMapper.insert(user);
        log.info("{}",user);
    }

    @Test
    public void update() {
        User user = new User();
        user.setId(1);
        user.setUserName(UUID.randomUUID().toString().substring(0,10));
        user.setAge(22);
        userMapper.update(user);
        log.info("{}",user);
    }

    @Test
    public void delete() {
        userMapper.delete(11);
    }

    @Test
    public void countByUserName(){
        User user = new User();
        user.setUserName("aa");
        int count = userMapper.countByUserName(user);
        log.info("{}",count);
    }
}