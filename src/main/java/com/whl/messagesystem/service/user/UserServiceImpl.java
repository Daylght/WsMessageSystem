package com.whl.messagesystem.service.user;

import com.alibaba.fastjson.JSONObject;
import com.whl.messagesystem.commons.channel.Channel;
import com.whl.messagesystem.commons.channel.management.user.UserRecoverListWithoutAdminChannel;
import com.whl.messagesystem.commons.channel.management.user.UserWithAdminListChannel;
import com.whl.messagesystem.commons.channel.management.user.UserWithoutAdminListChannel;
import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.commons.utils.WsResultUtil;
import com.whl.messagesystem.dao.GroupDao;
import com.whl.messagesystem.dao.UserAdminDao;
import com.whl.messagesystem.dao.UserDao;
import com.whl.messagesystem.dao.UserGroupDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.SessionInfo;
import com.whl.messagesystem.model.dto.UserGroupInfoDTO;
import com.whl.messagesystem.model.entity.*;
import com.whl.messagesystem.service.group.GroupService;
import com.whl.messagesystem.service.message.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.whl.messagesystem.commons.constant.StringConstant.SESSION_INFO;

/**
 * @author whl
 * @date 2021/12/7 18:48
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    GroupService groupService;

    @Resource
    MessageServiceImpl messageService;

    @Resource
    UserDao userDao;

    @Resource
    GroupDao groupDao;

    @Resource
    UserGroupDao userGroupDao;

    @Resource
    UserAdminDao userAdminDao;

    /**
     * 新用户注册
     */
    @Override
    public ResponseEntity<Result> register(String userName, String password, String adminId) {
        try {
            if (StringUtils.isAnyEmpty(userName, password, adminId)) {
                throw new NullPointerException("参数为空");
            }

            User user = new User();
            user.setUserName(userName);
            user.setPassword(password);
            log.info("要插入的用户信息为: {}", user);

            if (userDao.getUserCountByUserName(userName) > 0) {
                log.error("用户已存在");
                return ResponseEntity.ok(new Result(ResultEnum.ERROR.getStatus(), "用户已存在", null));
            }

            if (userDao.insertAnUser(user)) {
                String userId = userDao.getUserIdWithName(userName);
                UserAdmin userAdmin = new UserAdmin(userId, adminId);
                if (userAdminDao.insertAnUserAdmin(userAdmin)) {
                    user.setUserId(userId);
                    user.setShowStatus(0);
                    String message = JSONObject.toJSONString(WsResultUtil.registerUser(user));
                    Channel userWithAdminListChannel = new UserWithAdminListChannel(adminId);
                    messageService.publish(userWithAdminListChannel.getChannelName(), new TextMessage(message));

                    return ResponseEntity.ok(ResultUtil.success());
                }
            }

            throw new SQLException("user表插入新用户失败 || user_admin表插入关系失败");
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 更新用户名、密码
     */
    @Override
    public ResponseEntity<Result> updateUserNameAndPassword(String userId, String userName, String password, HttpSession session) {
        try {
            if (StringUtils.isAnyEmpty(userId, userName, password)) {
                throw new NullPointerException("参数为空");
            }

            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setPassword(password);

            if (userDao.updateUserNameAndPassword(user)) {
                // 更新会话
                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                sessionInfo.setUser(user);
                session.setAttribute(SESSION_INFO, sessionInfo);

                String message = JSONObject.toJSONString(WsResultUtil.updateUserNameAndPassword(user));
                Channel channel;
                if (sessionInfo.getGroup() == null) {
                    channel = new UserWithoutAdminListChannel();
                } else {
                    channel = new UserWithAdminListChannel(sessionInfo.getAdmin().getAdminId());
                }
                messageService.publish(channel.getChannelName(), new TextMessage(message));

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user表更新用户记录失败");
        } catch (Exception e) {
            log.error("更新用户信息失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 删除用户
     *
     * @param userId 用户id
     */
    @Override
    public ResponseEntity<Result> logicalDeleteUser(int userId) {
        try {
            if (userId == 0) {
                throw new NullPointerException("参数为空");
            }

            if (userDao.logicalDeleteAnUser(userId)) {
                UserAdmin userAdmin = userAdminDao.selectUserAdminByUserId(userId);
                User user = userDao.selectUserWithUserId(userId);
                UserGroup userGroup = userGroupDao.selectUserGroupByUserId(userId);

                // 该用户在分组中，执行踢出组员的方法
                if (userGroup != null) {
                    groupService.kickGroupMember(String.valueOf(userId));
                }

                String message = JSONObject.toJSONString(WsResultUtil.logicDeleteUser(user));
                TextMessage textMessage = new TextMessage(message);

                // 该用户不属于某个管理员，实时更新"回收站中的不属于管理员的用户"和"未指定管理员的用户"两个列表
                if (userAdmin == null) {
                    Channel userWithoutAdminListChannel = new UserWithoutAdminListChannel();
                    messageService.publish(userWithoutAdminListChannel.getChannelName(), textMessage);

                    Channel userRecoverListWithoutAdminChannel = new UserRecoverListWithoutAdminChannel();
                    messageService.publish(userRecoverListWithoutAdminChannel.getChannelName(), textMessage);
                } else {
                    Channel userWithAdminListChannel = new UserWithAdminListChannel(userAdmin.getAdminId());
                    messageService.publish(userWithAdminListChannel.getChannelName(), textMessage);
                }

                return ResponseEntity.ok(ResultUtil.success(user));
            }

            throw new SQLException("user表逻辑删除用户失败");
        } catch (Exception e) {
            log.error("逻辑删除用户失败，参数：{}，异常信息：{}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    /**
     * 永久删除用户
     *
     * @param userId
     */
    @Override
    public ResponseEntity<Result> completelyDeleteUser(int userId) {
        try {
            if (userId == 0) {
                throw new NullPointerException("参数为空");
            }

            User user = userDao.selectUserWithUserId(userId);
            UserAdmin userAdmin = userAdminDao.selectUserAdminByUserId(userId);

            // 删除user表中的用户记录以及user_admin表中的关系记录
            if (userDao.completelyDeleteUser(userId) && userAdminDao.deleteUserAdminByUserId(userId) >= 0) {
                // 用户不属于管理员，实时更新"回收站中未指定管理员的用户列表"
                if (userAdmin == null) {
                    String message = JSONObject.toJSONString(WsResultUtil.completeDeleteUser(user));
                    Channel channel = new UserRecoverListWithoutAdminChannel();
                    messageService.publish(channel.getChannelName(), new TextMessage(message));
                }

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user表永久删除用户失败");
        } catch (Exception e) {
            log.error("永久删除用户失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> recoverUser(int userId) {
        try {
            if (userId == 0) {
                throw new NullPointerException("参数为空");
            }

            if (userDao.recoverUser(userId)) {
                UserAdmin userAdmin = userAdminDao.selectUserAdminByUserId(userId);
                User user = userDao.selectUserWithUserId(userId);

                if (userAdmin == null) {
                    // 该用户不属于某个管理员，实时更新"回收站中的不属于管理员的用户"和"未指定管理员的用户"两个列表
                    String message = JSONObject.toJSONString(WsResultUtil.recoverUser(user));
                    TextMessage textMessage = new TextMessage(message);

                    Channel userRecoverListWithoutAdminChannel = new UserRecoverListWithoutAdminChannel();
                    messageService.publish(userRecoverListWithoutAdminChannel.getChannelName(), textMessage);

                    Channel userWithoutAdminListChannel = new UserWithoutAdminListChannel();
                    messageService.publish(userWithoutAdminListChannel.getChannelName(), textMessage);
                }

                return ResponseEntity.ok(ResultUtil.success(user));
            }

            throw new SQLException("user表恢复用户失败");
        } catch (Exception e) {
            log.error("恢复用户失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listUsersByAdminId(String adminId) {
        try {
            if (StringUtils.isEmpty(adminId)) {
                throw new NullPointerException("参数为空");
            }

            List<User> users = userDao.selectUsersWithAdminId(Integer.parseInt(adminId));
            List<Group> groups = users.stream().map(user -> groupDao.selectGroupByUserId(Integer.parseInt(user.getUserId()))).collect(Collectors.toList());
            List<UserGroupInfoDTO> userGroupInfoDTOList = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                userGroupInfoDTOList.add(new UserGroupInfoDTO(users.get(i), groups.get(i)));
            }
            userGroupInfoDTOList.forEach(userGroupInfoDTO -> {
                if (userGroupInfoDTO.getGroup() == null) {
                    Group group = groupDao.selectGroupByCreatorId(Integer.parseInt(userGroupInfoDTO.getUser().getUserId()));
                    if (group != null) {
                        userGroupInfoDTO.setGroup(group);
                    }
                }
            });

            return ResponseEntity.ok(ResultUtil.success(userGroupInfoDTOList));
        } catch (Exception e) {
            log.error("查询用户列表失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listUsersWithoutAdmin() {
        try {
            List<User> users = userDao.selectUsersWithoutAdmin();
            List<Group> groups = users.stream().map(user -> groupDao.selectGroupByUserId(Integer.parseInt(user.getUserId()))).collect(Collectors.toList());
            List<UserGroupInfoDTO> userGroupInfoDTOList = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                userGroupInfoDTOList.add(new UserGroupInfoDTO(users.get(i), groups.get(i)));
            }
            userGroupInfoDTOList.forEach(userGroupInfoDTO -> {
                if (userGroupInfoDTO.getGroup() == null) {
                    Group group = groupDao.selectGroupByCreatorId(Integer.parseInt(userGroupInfoDTO.getUser().getUserId()));
                    if (group != null) {
                        userGroupInfoDTO.setGroup(group);
                    }
                }
            });

            return ResponseEntity.ok(ResultUtil.success(userGroupInfoDTOList));
        } catch (Exception e) {
            log.error("查询未指定管理员的用户列表失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> giveUpManageUser(UserGroup userGroup, HttpSession session) {
        try {
            if (ObjectUtils.isEmpty(userGroup)) {
                throw new NullPointerException("参数为空");
            }

            String userId = userGroup.getUserId();

            if (groupDao.selectGroupCountByCreatorId(Integer.parseInt(userId)) == 1 || userGroupDao.selectUserGroupCountByUserId(Integer.parseInt(userId)) == 1) {
                // 该用户是组长或是组员，对该组进行放弃管理的操作
                String groupId = userGroup.getGroupId();
                return groupService.giveUpManagePrivateGroup(groupId, session);
            } else {
                // 该用户未加入分组，删除该用户和管理员的关系
                userAdminDao.deleteUserAdminByUserId(Integer.parseInt(userId));

                // 实时更新"没有指定管理员的用户"列表
                User user = userDao.selectUserWithUserId(Integer.parseInt(userId));
                String message = JSONObject.toJSONString(WsResultUtil.giveUpManageUser(user));
                Channel channel = new UserWithoutAdminListChannel();
                messageService.publish(channel.getChannelName(), new TextMessage(message));

                return ResponseEntity.ok(ResultUtil.success());
            }
        } catch (Exception e) {
            log.error("管理员放弃管理用户失败，参数：{}，异常信息：{}", userGroup, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> choiceUserToManage(UserGroup userGroup, HttpSession session) {
        try {
            if (ObjectUtils.isEmpty(userGroup)) {
                throw new NullPointerException("参数为空");
            }

            String userId = userGroup.getUserId();

            // 用户是组长或是组员，需要对整个组执行管理的操作
            if (groupDao.selectGroupCountByCreatorId(Integer.parseInt(userId)) == 1 || userGroupDao.selectUserGroupCountByUserId(Integer.parseInt(userId)) == 1) {
                return groupService.choiceManagePrivateGroup(userGroup.getGroupId(), session);
            } else {
                // 用户未加入分组，对他单独进行管理的操作
                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                String adminId = sessionInfo.getAdmin().getAdminId();
                userAdminDao.insertAnUserAdmin(new UserAdmin(userId, adminId));

                User user = userDao.selectUserWithUserId(Integer.parseInt(userId));
                String message = JSONObject.toJSONString(WsResultUtil.choiceManageUser(user));
                Channel userWithoutAdminListChannel = new UserWithoutAdminListChannel();
                messageService.publish(userWithoutAdminListChannel.getChannelName(), new TextMessage(message));

                return ResponseEntity.ok(ResultUtil.success(user));
            }
        } catch (Exception e) {
            log.error("管理员选择一个未指定管理员的用户进行管理失败，参数：{}，异常信息：{}", userGroup, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }
}
