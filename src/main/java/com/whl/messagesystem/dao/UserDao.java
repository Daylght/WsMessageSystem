package com.whl.messagesystem.dao;

import com.whl.messagesystem.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author whl
 * @date 2021/12/7 23:14
 */
@Mapper
public interface UserDao {
    /**
     * 往user表中插入一条新的记录
     */
    Boolean insertAnUser(User user);
}
