package com.whl.messagesystem;

import com.whl.messagesystem.dao.GroupDao;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class WsMessageSystemApplicationTests {

    @Resource
    GroupDao groupDao;

    @Resource
    UserDao userDao;

    @Test
    void contextLoads() {
    }

    @Test
    void selectGroup() {
        Group group1 = groupDao.selectGroupByGroupId(2);
        Group group2 = groupDao.selectGroupByGroupId(Integer.parseInt("2"));
        System.out.println(group1);
        assert group1.equals(group2);
    }

    @Test
    void listUser() {
        List<User> usersWithoutAdmin = userDao.selectUsersWithoutAdmin();
        System.out.println(usersWithoutAdmin);
        System.out.println("-----------");
        List<User> usersWithAdminId = userDao.selectUsersWithAdminId(1);
        System.out.println(usersWithAdminId);
    }

}
