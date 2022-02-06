package com.whl.messagesystem.service.group;

import com.alibaba.fastjson.JSONObject;
import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.commons.utils.WsResultUtil;
import com.whl.messagesystem.dao.AdminDao;
import com.whl.messagesystem.dao.GroupDao;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.dao.UserGroupDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.CreateGroupDto;
import com.whl.messagesystem.model.dto.SessionInfo;
import com.whl.messagesystem.model.entity.Group;
import com.whl.messagesystem.model.entity.User;
import com.whl.messagesystem.model.entity.UserGroup;
import com.whl.messagesystem.model.vo.GroupVo;
import com.whl.messagesystem.service.message.WebsocketEndPoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.bind.ValidationException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.whl.messagesystem.commons.constant.StringConstant.NO_GROUP;
import static com.whl.messagesystem.commons.constant.StringConstant.SESSION_INFO;

/**
 * @author whl
 * @date 2021/12/21 22:03
 */
@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    private static final int DEFAULT_MEMBER_COUNT = 20;

    @Resource
    GroupDao groupDao;

    @Resource
    UserGroupDao userGroupDao;

    @Resource
    AdminDao adminDao;

    @Resource
    UserDao userDao;

    @Resource
    WebsocketEndPoint websocketEndPoint;


    @Override
    public ResponseEntity<Result> getGroupsList() {
        try {
            List<GroupVo> groupVos = groupDao.selectAllGroupsAndCreators();
            return ResponseEntity.ok(ResultUtil.success(groupVos));
        } catch (Exception e) {
            log.error("获取分组列表异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

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


    @Override
    public ResponseEntity<Result> createGroup(CreateGroupDto createGroupDto) {
        // todo:这个方法或许要加锁，看后序情况
        try {
            if (ObjectUtils.isEmpty(createGroupDto)) {
                throw new ValidationException("参数为空");
            }

            String groupName = createGroupDto.getGroupName();
            String creatorId = createGroupDto.getCreatorId();
            String adminId = createGroupDto.getAdminId();
            int maxCount = createGroupDto.getMaxCount();

            /*
            在插入记录前先判断有没有组重名的情况
             */
            if (isExistGroup(groupName)) {
                log.warn("该组名已被使用");
                return ResponseEntity.ok(new Result(ResultEnum.ERROR.getStatus(), "该组名已被使用!", null));
            }

            /*
            然后判断当前用户是不是已经创建了组
             */
            if (groupDao.selectGroupCountByCreatorId(Integer.parseInt(creatorId)) > 0) {
                log.warn("该用户已创建过分组");
                return ResponseEntity.ok(new Result(ResultEnum.ERROR.getStatus(), "该用户已创建过分组", null));
            }

            /*
            如果未指定分组的人数上限，则默认为20
             */
            if (groupDao.insertAGroup(groupName, creatorId, adminId, maxCount == 0 ? DEFAULT_MEMBER_COUNT : maxCount)) {
                // 查出本组的信息并传给前端
                Group group = groupDao.findGroupByGroupName(groupName);
                GroupVo groupVo = new GroupVo(group);
                groupVo.setAdminName(adminDao.selectAdminByUserId(Integer.parseInt(creatorId)).getAdminName());
                groupVo.setCreatorName(userDao.selectUserWithUserId(Integer.parseInt(creatorId)).getUserName());

                String message = JSONObject.toJSONString(WsResultUtil.createGroup(groupVo));

                // 向大厅广播刚创建的分组的相关信息
                websocketEndPoint.publish(NO_GROUP + adminId, new TextMessage(message));

                return ResponseEntity.ok(ResultUtil.success(group));
            }

            throw new SQLException("group表插入记录失败");
        } catch (Exception e) {
            log.error("创建分组失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> remove(int[] groupIds, HttpSession session) {
        try {
            if (ArrayUtils.isEmpty(groupIds)) {
                throw new ValidationException("参数为空");
            }

            if (groupDao.deleteGroups(groupIds)) {
                log.info("删除成功");

                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                String adminId = sessionInfo.getAdmin().getAdminId();
                String message = JSONObject.toJSONString(WsResultUtil.deleteGroup(groupIds));

                // 向大厅广播被删除的分组id
                websocketEndPoint.publish(NO_GROUP + adminId, new TextMessage(message));

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("group表批量删除记录失败");
        } catch (Exception e) {
            log.error("批量删除分组失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> updateGroupInfo(Group group) {
        try {
            if (ObjectUtils.isEmpty(group)) {
                throw new ValidationException("参数为空");
            }

            if (groupDao.updateGroup(group) > 0) {
                log.info("更新成功");
                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("group表更新失败");
        } catch (Exception e) {
            log.error("修改分组信息失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> joinGroup(UserGroup userGroup, HttpSession session) {
        try {
            if (ObjectUtils.isEmpty(userGroup)) {
                throw new ValidationException("参数为空");
            }

            if (userGroupDao.selectUserGroupCountByUserId(Integer.parseInt(userGroup.getUserId())) != 0) {
                log.warn("该用户已经在分组内");
                return ResponseEntity.ok(new Result(ResultEnum.ERROR.getStatus(), "该用户已经在分组内", null));
            }

            // todo:未来这里要做websocket，通知加入分组的事件
            if (userGroupDao.insertAnUserGroup(userGroup)) {
                Group group = groupDao.selectGroupByGroupId(Integer.parseInt(userGroup.getGroupId()));

                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                sessionInfo.setGroup(group);
                session.setAttribute(SESSION_INFO, sessionInfo);
                String userName = sessionInfo.getUser().getUserName();

                Map<String, Object> data = new HashMap<>();
                data.put("userName", userName);
                data.put("userId", userGroup.getUserId());
                data.put("groupId", userGroup.getGroupId());

                String message = JSONObject.toJSONString(WsResultUtil.joinGroup(data));
                websocketEndPoint.publish(group.getGroupName(), new TextMessage(message));

                log.info("加入分组成功");
                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user_group表插入记录失败");
        } catch (Exception e) {
            log.error("加入分组失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listGroupMembers(String groupId, HttpSession session) {
        try {
            if (StringUtils.isEmpty(groupId)) {
                throw new NullPointerException("参数为空");
            }

            List<User> groupMembersList = groupDao.selectUsersWithGroupId(Integer.parseInt(groupId));

            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
            int creatorId = Integer.parseInt(sessionInfo.getGroup().getCreatorId());
            User creator = userDao.selectUserWithUserId(creatorId);
            creator.setPassword(null);

            Map<String, Object> groupMembersAndCreator = new HashMap<>(groupMembersList.size() + 1);
            groupMembersAndCreator.put("creator", creator);
            groupMembersAndCreator.put("members", groupMembersList);

            return ResponseEntity.ok(ResultUtil.success(groupMembersAndCreator));
        } catch (Exception e) {
            log.error("获取组员列表失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }
}
