package com.whl.messagesystem.service.group;

import com.alibaba.fastjson.JSONObject;
import com.whl.messagesystem.commons.channel.Channel;
import com.whl.messagesystem.commons.channel.group.PrivateGroupMessageChannel;
import com.whl.messagesystem.commons.channel.group.PublicGroupMessageChannel;
import com.whl.messagesystem.commons.channel.management.group.PrivateGroupWithoutAdminListChannel;
import com.whl.messagesystem.commons.channel.management.group.PublicGroupCreatedByOutsideListChannel;
import com.whl.messagesystem.commons.channel.management.user.UserWithAdminListChannel;
import com.whl.messagesystem.commons.channel.management.user.UserWithoutAdminListChannel;
import com.whl.messagesystem.commons.channel.user.GroupHallListChannel;
import com.whl.messagesystem.commons.constant.ResultEnum;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.commons.utils.WsResultUtil;
import com.whl.messagesystem.dao.*;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.model.dto.CreateGroupDTO;
import com.whl.messagesystem.model.dto.CreatePublicGroupDTO;
import com.whl.messagesystem.model.dto.OutsideCreatePublicGroupDTO;
import com.whl.messagesystem.model.dto.SessionInfo;
import com.whl.messagesystem.model.entity.*;
import com.whl.messagesystem.model.vo.GroupVO;
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
import javax.xml.bind.ValidationException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    PublicGroupDao publicGroupDao;

    @Resource
    UserGroupDao userGroupDao;

    @Resource
    AdminDao adminDao;

    @Resource
    UserDao userDao;

    @Resource
    UserAdminDao userAdminDao;

    @Resource
    MessageServiceImpl messageService;


    @Override
    public ResponseEntity<Result> getGroupsList() {
        try {
            List<GroupVO> groupVos = groupDao.selectAllGroupsAndCreators();
            return ResponseEntity.ok(ResultUtil.success(groupVos));
        } catch (Exception e) {
            log.error("获取分组列表异常: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> getGroupsListByAdminId(String adminId) {
        try {
            if (StringUtils.isEmpty(adminId)) {
                throw new NullPointerException("参数为空");
            }

            List<GroupVO> groupVOWithoutCreator = groupDao.selectGroupVOWithoutCreatorByAdminId(Integer.parseInt(adminId));
            List<GroupVO> groupVOWithCreator = groupDao.selectAllGroupsAndCreatorsByAdminId(Integer.parseInt(adminId));

            Stream<GroupVO> s1 = groupVOWithCreator.stream();
            Stream<GroupVO> s2 = groupVOWithoutCreator.stream();

            List<GroupVO> result = Stream.concat(s1, s2).sorted(Comparator.comparing(GroupVO::getGroupId)).collect(Collectors.toList());

            return ResponseEntity.ok(ResultUtil.success(result));
        } catch (Exception e) {
            log.error("获取用户列表失败，参数: {}，异常信息: {}", adminId, e.getMessage());
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
    public ResponseEntity<Result> createGroup(CreateGroupDTO createGroupDto, HttpSession session) {
        try {
            if (ObjectUtils.isEmpty(createGroupDto)) {
                throw new NullPointerException("参数为空");
            }

            String groupName = createGroupDto.getGroupName();
            String creatorId = createGroupDto.getCreatorId();
            String adminId = createGroupDto.getAdminId();
            int maxCount = createGroupDto.getMaxCount();
            Boolean adminCreated = createGroupDto.getAdminCreated();

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
            if (groupDao.insertAGroup(groupName, creatorId, adminId, maxCount == 0 ? DEFAULT_MEMBER_COUNT : maxCount, adminCreated)) {
                // 查出本组的信息并传给前端
                Group group = groupDao.findGroupByGroupName(groupName);
                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                sessionInfo.setGroup(group);
                GroupVO groupVo = new GroupVO(group);
                groupVo.setAdminName(adminDao.selectAdminByUserId(Integer.parseInt(creatorId)).getAdminName());
                groupVo.setCreatorName(userDao.selectUserWithUserId(Integer.parseInt(creatorId)).getUserName());

                String message = JSONObject.toJSONString(WsResultUtil.createGroup(groupVo));
                TextMessage textMessage = new TextMessage(message);
                // 向大厅广播刚创建的分组的相关信息
                Channel groupHallListChannel = new GroupHallListChannel(adminId);
                messageService.publish(groupHallListChannel.getChannelName(), textMessage);

                return ResponseEntity.ok(ResultUtil.success(group));
            }

            throw new SQLException("group表插入记录失败");
        } catch (Exception e) {
            log.error("用户创建私有分组失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public ResponseEntity<Result> remove(String groupId, HttpSession session) {
        try {
            if (StringUtils.isEmpty(groupId)) {
                throw new NullPointerException("参数为空");
            }

            Group group = groupDao.selectGroupByGroupId(Integer.parseInt(groupId));
            log.info("要删除的分组信息: {}", group);

            if (group != null && userGroupDao.deleteUserGroupsByGroupId(Integer.parseInt(groupId)) >= 0) {
                String message = JSONObject.toJSONString(WsResultUtil.deleteGroup(group));
                TextMessage textMessage = new TextMessage(message);

                // 向被删除的分组内广播消息
                Channel privateGroupMessageChannel = new PrivateGroupMessageChannel(group.getGroupName());
                messageService.publish(privateGroupMessageChannel.getChannelName(), textMessage);
                // 删除分组内的ws连接
                messageService.deleteChannel(privateGroupMessageChannel.getChannelName());

                if (groupDao.deleteGroupByGroupId(Integer.parseInt(groupId))) {
                    SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                    String adminId = sessionInfo.getAdmin().getAdminId();

                    // 向大厅广播被删除的分组id
                    Channel groupHallListChannel = new GroupHallListChannel(adminId);
                    messageService.publish(groupHallListChannel.getChannelName(), textMessage);

                    // 更新"未指定管理员的私有分组"列表
                    Channel privateGroupWithoutAdminListChannel = new PrivateGroupWithoutAdminListChannel();
                    messageService.publish(privateGroupWithoutAdminListChannel.getChannelName(), textMessage);

                    return ResponseEntity.ok(ResultUtil.success());
                }
            }

            throw new SQLException("group表删除记录失败");
        } catch (Exception e) {
            log.error("删除分组失败，参数: {}，异常信息: {}", groupId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> updateGroupInfo(Group group) {
        try {
            if (ObjectUtils.isEmpty(group)) {
                throw new NullPointerException("参数为空");
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
                data.put("groupName", group.getGroupName());

                // 组内广播此人加入的消息
                String message = JSONObject.toJSONString(WsResultUtil.joinGroup(data));
                TextMessage textMessage = new TextMessage(message);
                Channel privateGroupMessageChannel = new PrivateGroupMessageChannel(group.getGroupName());
                messageService.publish(privateGroupMessageChannel.getChannelName(), textMessage);

                Channel userWithAdminListChannel = new UserWithAdminListChannel(sessionInfo.getAdmin().getAdminId());
                messageService.publish(userWithAdminListChannel.getChannelName(), textMessage);

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user_group表插入记录失败");
        } catch (Exception e) {
            log.error("加入分组失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> quitGroup(int userId, HttpSession session) {
        try {
            if (userGroupDao.deleteAnUserGroupByUserId(userId)) {
                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                String groupName = sessionInfo.getGroup().getGroupName();

                Map<String, Object> data = new HashMap<>(3);
                data.put("userName", sessionInfo.getUser().getUserName());
                data.put("userId", userId);
                data.put("groupId", sessionInfo.getGroup().getGroupId());

                sessionInfo.setGroup(null);

                // 向组内广播此人退出的消息
                String message = JSONObject.toJSONString(WsResultUtil.quitGroup(data));
                TextMessage textMessage = new TextMessage(message);
                Channel privateGroupMessageChannel = new PrivateGroupMessageChannel(groupName);
                messageService.publish(privateGroupMessageChannel.getChannelName(), textMessage);

                // 向管理员广播此人退出的消息
                Channel userWithAdminListChannel = new UserWithAdminListChannel(sessionInfo.getAdmin().getAdminId());
                messageService.publish(userWithAdminListChannel.getChannelName(), textMessage);

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user_group表删除关系失败");
        } catch (Exception e) {
            log.error("退出分组失败: {}", e.getMessage());
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
            if (sessionInfo.getGroup().getCreatorId() != null) {
                int creatorId = Integer.parseInt(sessionInfo.getGroup().getCreatorId());
                User creator = userDao.selectUserWithUserId(creatorId);
                creator.setPassword(null);

                Map<String, Object> groupMembersAndCreator = new HashMap<>();
                groupMembersAndCreator.put("creator", creator);
                groupMembersAndCreator.put("members", groupMembersList);

                return ResponseEntity.ok(ResultUtil.success(groupMembersAndCreator));
            } else {
                Map<String, Object> groupMembersAndCreator = new HashMap<>();
                groupMembersAndCreator.put("creator", null);
                groupMembersAndCreator.put("members", groupMembersList);

                return ResponseEntity.ok(ResultUtil.success(groupMembersAndCreator));
            }


        } catch (Exception e) {
            log.error("获取组员列表失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> createPublicGroup(CreatePublicGroupDTO createPublicGroupDTO) {
        try {
            if (ObjectUtils.isEmpty(createPublicGroupDTO)) {
                throw new NullPointerException("参数为空");
            }

            String groupName = createPublicGroupDTO.getGroupName();
            Integer maxCount = createPublicGroupDTO.getMaxCount();
            String adminId = createPublicGroupDTO.getAdminId();

            // 不允许创建同名的公共分组
            if (isExistPublicGroup(groupName)) {
                log.warn("该组名已被使用");
                return ResponseEntity.ok(new Result<>(ResultEnum.ERROR.getStatus(), "该组名已被使用!", null));
            }

            PublicGroup publicGroup = new PublicGroup();
            publicGroup.setGroupName(groupName);
            publicGroup.setMaxCount(maxCount == 0 ? DEFAULT_MEMBER_COUNT : maxCount);
            publicGroup.setAdminCreated(true);
            publicGroup.setAdminId(adminId);

            if (publicGroupDao.insertPublicGroup(publicGroup)) {
                publicGroup = publicGroupDao.selectPublicGroupByName(groupName);
                // 管理员创建公共分组后是不需要进行广播的，因为只有他才能看见自己创建的公共分组列表
                return ResponseEntity.ok(ResultUtil.success(publicGroup));
            }

            throw new SQLException("public_group表插入记录失败");
        } catch (Exception e) {
            log.error("创建公共分组失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    private boolean isExistPublicGroup(String groupName) {
        try {
            if (publicGroupDao.selectPublicGroupCountByName(groupName) == 0) {
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("查找该公共组是否存在失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public ResponseEntity<Result> kickGroupMember(String userId) {
        try {
            if (StringUtils.isEmpty(userId)) {
                throw new NullPointerException("参数为空");
            }

            String groupName = groupDao.selectGroupByUserId(Integer.parseInt(userId)).getGroupName();

            if (userGroupDao.deleteAnUserGroupByUserId(Integer.parseInt(userId))) {
                // 向组内广播此人被踢出的消息
                Channel channel = new PrivateGroupMessageChannel(groupName);
                String message = JSONObject.toJSONString(WsResultUtil.kickMember(userId));
                messageService.publish(channel.getChannelName(), new TextMessage(message));

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user_group表删除记录失败");
        } catch (Exception e) {
            log.error("踢出用户失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listPublicGroupsCreatedByAdmin(String adminId) {
        try {
            if (StringUtils.isEmpty(adminId) || "0".equals(adminId)) {
                throw new NullPointerException("参数为空");
            }

            List<PublicGroup> publicGroups = publicGroupDao.selectPublicGroupsWithAdminId(Integer.parseInt(adminId));
            return ResponseEntity.ok(ResultUtil.success(publicGroups));
        } catch (Exception e) {
            log.error("获取管理员创建的公共分组列表失败，管理员id: {}，异常信息: {}", adminId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listPublicGroupsCreatedByOutside() {
        try {
            List<PublicGroup> publicGroups = publicGroupDao.selectPublicGroupsCreatedByOutside();
            return ResponseEntity.ok(ResultUtil.success(publicGroups));
        } catch (Exception e) {
            log.error("获取管理员创建的公共分组列表失败，异常信息: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> dismissGroup(String groupId, HttpSession session) {
        try {
            if (StringUtils.isEmpty(groupId)) {
                throw new NullPointerException("参数为空");
            }

            if (userGroupDao.deleteUserGroupsByGroupId(Integer.parseInt(groupId)) >= 0 && groupDao.deleteGroupByGroupId(Integer.parseInt(groupId))) {
                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                String groupName = sessionInfo.getGroup().getGroupName();
                String adminId = sessionInfo.getAdmin().getAdminId();
                sessionInfo.setGroup(null);

                Map<String, Object> map = new HashMap<>(2);
                map.put("creatorId", sessionInfo.getUser().getUserId());
                map.put("groupId", groupId);
                String deleteGroupMessage = JSONObject.toJSONString(WsResultUtil.dismissGroup(map));
                TextMessage textMessage = new TextMessage(deleteGroupMessage);

                Channel privateGroupMessageChannel = new PrivateGroupMessageChannel(groupName);
                messageService.publish(privateGroupMessageChannel.getChannelName(), textMessage);
                // 删除私有分组内的所有websocket连接
                messageService.deleteChannel(privateGroupMessageChannel.getChannelName());

                Channel groupHallListChannel = new GroupHallListChannel(adminId);
                messageService.publish(groupHallListChannel.getChannelName(), textMessage);

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("user_group表删除关系失败 || group表删除记录失败");
        } catch (Exception e) {
            log.error("解散分组失败，组id: {}，异常信息: {}", groupId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> dismissPublicGroup(String groupId) {
        try {
            if (StringUtils.isEmpty(groupId)) {
                throw new NullPointerException("参数为空");
            }

            PublicGroup publicGroup = publicGroupDao.selectPublicGroupById(Integer.parseInt(groupId));
            if (ObjectUtils.isEmpty(publicGroup)) {
                return ResponseEntity.ok(new Result<>(ResultEnum.ERROR.getStatus(), "该分组不存在或已被解散", null));
            }

            boolean adminCreated = publicGroup.isAdminCreated();

            if ((publicGroupDao.deletePublicGroupById(Integer.parseInt(groupId)) == 1)) {
                // 如果不是某个管理员创建的分组，那么其它管理员也应该实时更新组列表，因为所有的管理员都能看到组列表
                if (!adminCreated) {
                    Channel channel = new PublicGroupCreatedByOutsideListChannel();
                    String message = JSONObject.toJSONString(WsResultUtil.dismissPublicGroup(new HashMap<String, Object>(1).put("groupId", groupId)));
                    messageService.publish(channel.getChannelName(), new TextMessage(message));
                }

                // 删除这个公共分组的全部websocket连接
                PublicGroupMessageChannel publicGroupMessageChannel = new PublicGroupMessageChannel(publicGroup.getGroupName());
                messageService.deleteChannel(publicGroupMessageChannel.getChannelName());

                // 如果是管理员创建的分组，不需要通过ws进行列表实时更新，因为只有一个管理员能看到组列表
                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("public_group表删除记录失败");
        } catch (Exception e) {
            log.error("解散公共分组失败，组id: {}，异常信息: {}", groupId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> outsideCreatePublicGroup(OutsideCreatePublicGroupDTO outsideCreatePublicGroupDTO) {
        try {
            if (ObjectUtils.isEmpty(outsideCreatePublicGroupDTO)) {
                throw new NullPointerException("参数为空");
            }

            String groupName = outsideCreatePublicGroupDTO.getGroupName();
            Integer maxCount = outsideCreatePublicGroupDTO.getMaxCount();

            // 不允许创建同名的公共分组
            if (isExistPublicGroup(groupName)) {
                log.warn("该组名已被使用");
                return ResponseEntity.ok(new Result<>(ResultEnum.ERROR.getStatus(), "该组名已被使用!", null));
            }

            PublicGroup publicGroup = new PublicGroup();
            publicGroup.setGroupName(groupName);
            publicGroup.setMaxCount(maxCount == 0 ? DEFAULT_MEMBER_COUNT : maxCount);
            publicGroup.setAdminCreated(false);
            publicGroup.setAdminId(null);

            if (publicGroupDao.insertPublicGroup(publicGroup)) {
                publicGroup = publicGroupDao.selectPublicGroupByName(groupName);

                // 实时更新管理员端"外部创建的公共分组"列表
                Channel channel = new PublicGroupCreatedByOutsideListChannel();
                String message = JSONObject.toJSONString(WsResultUtil.createPublicGroup(publicGroup));
                messageService.publish(channel.getChannelName(), new TextMessage(message));

                // 管理员创建公共分组后是不需要进行广播的，因为只有他才能看见自己创建的公共分组列表
                return ResponseEntity.ok(ResultUtil.success(publicGroup));
            }

            throw new SQLException("public_group表插入记录失败");
        } catch (Exception e) {
            log.error("外部调用创建公共分组失败，参数: {}，异常信息: {}", outsideCreatePublicGroupDTO, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> listGroupsWithoutAdmin() {
        try {
            List<GroupVO> groupVos = groupDao.selectAllGroupsAndCreatorsWithoutAdmin();
            return ResponseEntity.ok(ResultUtil.success(groupVos));
        } catch (Exception e) {
            log.error("获取未指定管理员的分组列表失败，异常信息: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> adminCreateGroup(CreateGroupDTO createGroupDTO, HttpSession session) {
        try {
            if (ObjectUtils.isEmpty(createGroupDTO)) {
                log.error("参数为空");
                throw new NullPointerException("参数为空");
            }

            String groupName = createGroupDTO.getGroupName();
            String creatorId = createGroupDTO.getCreatorId();
            String adminId = createGroupDTO.getAdminId();
            int maxCount = createGroupDTO.getMaxCount();
            Boolean adminCreated = createGroupDTO.getAdminCreated();

            /*
            在插入记录前先判断有没有组重名的情况
             */
            if (isExistGroup(groupName)) {
                log.warn("该组名已被使用");
                return ResponseEntity.ok(new Result(ResultEnum.ERROR.getStatus(), "该组名已被使用!", null));
            }

            /*
            如果未指定分组的人数上限，则默认为20
             */
            if (groupDao.insertAGroup(groupName, creatorId, adminId, maxCount == 0 ? DEFAULT_MEMBER_COUNT : maxCount, adminCreated)) {
                // 查出本组的信息并传给前端
                Group group = groupDao.findGroupByGroupName(groupName);
                GroupVO groupVo = new GroupVO(group);
                SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
                Admin admin = sessionInfo.getAdmin();
                groupVo.setAdminName(admin.getAdminName());

                // 向大厅广播刚创建的分组的相关信息
                String message = JSONObject.toJSONString(WsResultUtil.createGroup(groupVo));
                Channel channel = new GroupHallListChannel(adminId);
                messageService.publish(channel.getChannelName(), new TextMessage(message));

                return ResponseEntity.ok(ResultUtil.success(group));
            }

            throw new SQLException("group表插入记录失败");
        } catch (Exception e) {
            log.error("管理员创建私有分组失败: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> giveUpManagePrivateGroup(String groupId, HttpSession session) {
        try {
            if (StringUtils.isEmpty(groupId)) {
                throw new NullPointerException("参数为空");
            }

            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
            String adminId = sessionInfo.getAdmin().getAdminId();
            log.info("adminId: {}", adminId);

            GroupVO groupVO = groupDao.selectGroupVOByGroupId(Integer.parseInt(groupId));
            List<User> users = groupDao.selectUsersWithGroupId(Integer.parseInt(groupId));
            User creator = userDao.selectUserWithUserId(Integer.parseInt(groupVO.getCreatorId()));
            users.add(creator);

            if (groupDao.clearAdminId(Integer.parseInt(groupId))) {
                users.forEach(user -> userAdminDao.deleteUserAdminByUserId(Integer.parseInt(user.getUserId())));

                // 构造放弃管理分组的消息
                String message = JSONObject.toJSONString(WsResultUtil.giveUpManageGroup(groupId));
                TextMessage textMessage = new TextMessage(message);

                // 向被放弃管理的分组中广播消息
                Channel privateGroupMessageChannel = new PrivateGroupMessageChannel(groupVO.getGroupName());
                messageService.publish(privateGroupMessageChannel.getChannelName(), textMessage);

                // 实时更新"未指定管理员的私有分组"列表
                Channel privateGroupWithoutAdminListChannel = new PrivateGroupWithoutAdminListChannel();
                messageService.publish(privateGroupWithoutAdminListChannel.getChannelName(), textMessage);

                // 更新大厅列表
                Channel groupHallListChannel = new GroupHallListChannel(adminId);
                messageService.publish(groupHallListChannel.getChannelName(), textMessage);

                // 实时更新"未指定管理员的用户"列表
                Map<String, Object> map = new HashMap<>();
                map.put("users", users);
                map.put("groupName", groupVO.getGroupName());
                String json = JSONObject.toJSONString(WsResultUtil.giveUpManageUser(map));
                Channel userWithoutAdminListChannel = new UserWithoutAdminListChannel();
                messageService.publish(userWithoutAdminListChannel.getChannelName(), new TextMessage(json));

                return ResponseEntity.ok(ResultUtil.success());
            }

            throw new SQLException("group表清空adminId失败 || user_admin表");
        } catch (Exception e) {
            log.error("放弃管理私有分组失败，参数: {}，异常信息: {}", groupId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }

    @Override
    public ResponseEntity<Result> choiceManagePrivateGroup(String groupId, HttpSession session) {
        try {
            if (StringUtils.isEmpty(groupId)) {
                throw new NullPointerException("参数为空");
            }

            Group group = groupDao.selectGroupByGroupId(Integer.parseInt(groupId));
            String creatorId = group.getCreatorId();
            SessionInfo sessionInfo = (SessionInfo) session.getAttribute(SESSION_INFO);
            String adminId = sessionInfo.getAdmin().getAdminId();

            if (groupDao.updateAdminIdByGroupId(Integer.parseInt(adminId), Integer.parseInt(groupId))) {
                // 把组长和组员的userId放入List中
                List<UserGroup> userGroups = userGroupDao.selectUserGroupsByGroupId(Integer.parseInt(groupId));
                List<String> userIds = userGroups.stream().map(UserGroup::getUserId).collect(Collectors.toList());
                userIds.add(creatorId);

                // 向user_admin表插入关系
                userIds.forEach(userId -> userAdminDao.insertAnUserAdmin(new UserAdmin(userId, adminId)));

                Map<String, Object> map = new HashMap<>(2);
                map.put("groupId", groupId);
                map.put("admin", sessionInfo.getAdmin());
                TextMessage textMessage = new TextMessage(JSONObject.toJSONString(WsResultUtil.choiceManagePrivateGroup(map)));

                // 向组内广播纳入管理的消息
                Channel privateGroupMessageChannel = new PrivateGroupMessageChannel(group.getGroupName());
                messageService.publish(privateGroupMessageChannel.getChannelName(), textMessage);

                // 实时更新"未指定管理员的私有分组"列表
                Channel privateGroupWithoutAdminListChannel = new PrivateGroupWithoutAdminListChannel();
                messageService.publish(privateGroupWithoutAdminListChannel.getChannelName(), textMessage);

                // 实时更新大厅中的组列表
                Channel groupHallListChannel = new GroupHallListChannel(adminId);
                messageService.publish(groupHallListChannel.getChannelName(), textMessage);

                // 实时更新"未指定管理员的用户"列表
                List<User> users = userDao.selectUsersWithAdminId(Integer.parseInt(adminId));
                String message = JSONObject.toJSONString(WsResultUtil.choiceManageUser(users));
                Channel userWithoutAdminListChannel = new UserWithoutAdminListChannel();
                messageService.publish(userWithoutAdminListChannel.getChannelName(), new TextMessage(message));

                group.setAdminId(adminId);

                return ResponseEntity.ok(ResultUtil.success(group));
            }

            throw new SQLException("更新user_group表异常 || 插入user_admin表异常");
        } catch (Exception e) {
            log.error("管理员选择管理未指定管理员的分组失败，参数：{}，异常信息：{}", groupId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResultUtil.error());
        }
    }
}
