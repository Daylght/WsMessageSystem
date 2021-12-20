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

    /**
     * 更新user表中的一条记录
     * @param user
     * @return
     */
    Boolean updateAnUser(User user);

    /**
     * 逻辑删除一个用户，即把user表中某条记录的showStatus置为1
     */
    Boolean logicalDeleteAnUser(String userName);
}
