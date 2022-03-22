package com.whl.messagesystem;

import com.whl.messagesystem.dao.GroupDao;
import com.whl.messagesystem.model.entity.Group;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class WsMessageSystemApplicationTests {

    @Resource
    GroupDao groupDao;

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

}
