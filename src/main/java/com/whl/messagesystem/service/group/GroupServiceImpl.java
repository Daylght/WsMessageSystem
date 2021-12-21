package com.whl.messagesystem.service.group;

import com.whl.messagesystem.dao.GroupDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author whl
 * @date 2021/12/21 22:03
 */
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    GroupDao groupDao;

    @Override
    public boolean isExistGroup(String groupName) {
        try {
            if (groupDao.findGroupByGroupName(groupName) != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error("查找该组是否存在失败: {}", e.getMessage());
            return false;
        }
    }
}
