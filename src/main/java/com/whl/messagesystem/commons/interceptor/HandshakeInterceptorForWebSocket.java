package com.whl.messagesystem.commons.interceptor;

import com.whl.messagesystem.commons.channel.Channel;
import com.whl.messagesystem.commons.channel.group.PrivateGroupMessageChannel;
import com.whl.messagesystem.commons.channel.group.PublicGroupMessageChannel;
import com.whl.messagesystem.commons.channel.management.group.PrivateGroupWithoutAdminListChannel;
import com.whl.messagesystem.commons.channel.management.group.PublicGroupCreatedByOutsideListChannel;
import com.whl.messagesystem.commons.channel.management.user.UserRecoverListWithoutAdminChannel;
import com.whl.messagesystem.commons.channel.management.user.UserWithoutAdminListChannel;
import com.whl.messagesystem.commons.channel.user.GroupHallListChannel;
import com.whl.messagesystem.commons.constant.GroupConstant;
import com.whl.messagesystem.commons.utils.ResultUtil;
import com.whl.messagesystem.dao.PublicGroupDao;
import com.whl.messagesystem.model.Result;
import com.whl.messagesystem.service.group.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author whl
 * @date 2021/12/21 21:27
 */
@Slf4j
@Component
public class HandshakeInterceptorForWebSocket implements HandshakeInterceptor {

    @Resource
    GroupService groupService;

    @Resource
    PublicGroupDao publicGroupDao;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        log.info("拦截器：beforeHandshake");
        try {
            //转换获取HttpServletRequest对象来获取请求参数
            final HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            //获取指定的请求参数
            final String scene = String.valueOf(httpServletRequest.getParameter("scene"));
            final String groupName = String.valueOf(httpServletRequest.getParameter(GroupConstant.GROUP_NAME));
            final String adminId = String.valueOf(httpServletRequest.getParameter("adminId"));

            Channel channel = null;

            switch (scene) {
                case "groupHallList": {
                    channel = new GroupHallListChannel(adminId);
                    break;
                }
                case "publicGroupCreatedByOutsideList": {
                    channel = new PublicGroupCreatedByOutsideListChannel();
                    break;
                }
                case "privateGroupMessage": {
                     /*
                      下面这一块if用于处理groupName是错误的组名的情况（没有这个组）
                     */
                    if (!groupService.isExistGroup(groupName) && groupName != null) {
                        //获取响应对象
                        final HttpServletResponse servletResponse = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
                        final Result<Object> responseMessage = ResultUtil.error("私有分组不存在");
                        //设置必要响应头并进行数据响应
                        servletResponse.setContentType("application/json;charset=UTF-8");
                        servletResponse.getWriter().print(responseMessage);
                        servletResponse.flushBuffer();
                        System.out.println("这个组不存在不能进行广播");
                        return false;
                    }

                    channel = new PrivateGroupMessageChannel(groupName);
                    break;
                }
                case "publicGroupMessage": {
                    if (publicGroupDao.selectPublicGroupCountByName(groupName) == 0) {
                        //获取响应对象
                        final HttpServletResponse servletResponse = ((ServletServerHttpResponse) serverHttpResponse).getServletResponse();
                        final Result<Object> responseMessage = ResultUtil.error("公共分组不存在");
                        //设置必要响应头并进行数据响应
                        servletResponse.setContentType("application/json;charset=UTF-8");
                        servletResponse.getWriter().print(responseMessage);
                        servletResponse.flushBuffer();
                        return false;
                    }

                    channel = new PublicGroupMessageChannel(groupName);
                    break;
                }
                case "privateGroupWithoutAdminList": {
                    channel = new PrivateGroupWithoutAdminListChannel();
                    break;
                }
                case "userWithoutAdminList": {
                    channel = new UserWithoutAdminListChannel();
                    break;
                }
                case "userRecoverListWithoutAdmin":{
                    channel = new UserRecoverListWithoutAdminChannel();
                    break;
                }
                default: {
                }
            }

            assert channel != null;
            String channelName = channel.getChannelName();
            map.put("channelName", channelName);

            return true;
        } catch (Exception e) {
            log.error("websocket拦截器异常: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
