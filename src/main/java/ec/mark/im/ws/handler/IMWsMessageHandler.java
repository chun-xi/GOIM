package ec.mark.im.ws.handler;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Aop;

import ec.mark.im.common.model.Groups;
import ec.mark.im.common.model.UserFriends;
import ec.mark.im.common.model.Users;
import ec.mark.im.common.vo.ChatMsg;
import ec.mark.im.common.vo.FriendState;
import ec.mark.im.common.vo.SystemMsg;
import ec.mark.im.service.GroupService;
import ec.mark.im.service.GroupUserService;
import ec.mark.im.service.UserFriendService;
import ec.mark.im.service.UserService;
import ec.mark.im.ws.WsServerConfig;

public class IMWsMessageHandler implements IWsMsgHandler {
	private static Logger log = LoggerFactory.getLogger(IMWsMessageHandler.class);
	public static final IMWsMessageHandler me = new IMWsMessageHandler();

	private IMWsMessageHandler() {

	}

	@Override
	public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext)
			throws Exception {
		String userId = httpRequest.getParam("userId");
		UserService userService = Aop.get(UserService.class);
		Users user = userService.findById(userId);
		if (user == null) {
			return null;
		}
		Tio.bindUser(channelContext, userId);
		log.info("用户握手:{}", httpRequest.getRemote());
		return httpResponse;
	}

	@Override
	public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext)
			throws Exception {
		String userid = channelContext.userid;
		GroupService groupService = Aop.get(GroupService.class);
		Users user = new Users();
		user.setId(Long.valueOf(userid));
		// 将用户绑定到群组
		// 告诉好友你上线了
		updateState(user, channelContext, 1);
		List<Groups> myGroups = groupService.getMyGroups(user);
		for (Groups group : myGroups) {
			Tio.bindGroup(channelContext, group.getId().toString());
		}
	}

	@Override
	public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		return null;
	}

	@Override
	public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
		// 用户断开设置用户离线
		String userid = channelContext.userid;
		UserService userService = Aop.get(UserService.class);
		userService.updateUserState(userid, 0);
		// 告诉你好友你拜拜了
		Users user = userService.findById(userid);
		updateState(user, channelContext, 0);
		return null;
	}

	@Override
	public Object onText(WsRequest wsRequest, String text, ChannelContext channelContext) throws Exception {
		JSONObject msg = JSON.parseObject(text);
		String type = msg.getString("type");
		if (type.equals("message")) {
			JSONObject mine = msg.getJSONObject("data").getJSONObject("mine");
			JSONObject to = msg.getJSONObject("data").getJSONObject("to");
			ChatMsg chatMsg = new ChatMsg();
			chatMsg.setMsgtype("message");
			chatMsg.setFromid(mine.getString("id"));
			chatMsg.setAvatar(mine.getString("avatar"));
			chatMsg.setUsername(mine.getString("username"));
			chatMsg.setContent(mine.getString("content"));
			chatMsg.setType(to.getString("type"));
			chatMsg.setTimestamp(new Date().getTime());
			log.info("用户:{}\t消息:{}\t类型:", mine.getString("username"), mine.getString("content"), to.getString("type"));
			if ("group".equals(chatMsg.getType())) {
				// 判断群状态 1:可以发言 0:禁言
				GroupUserService groupUserService = Aop.get(GroupUserService.class);
				boolean inGroup = groupUserService.isInGroup(mine.getLongValue("id"), to.getLongValue("id"));
				SystemMsg systemMsg = new SystemMsg();
				if (inGroup) {
					systemMsg.setSystem(true);
					systemMsg.setId(to.getIntValue("id"));
					systemMsg.setType(chatMsg.getType());
					systemMsg.setMsgtype("message");
					systemMsg.setContent("您已不在该群");
					WsResponse response = WsResponse.fromText(JSON.toJSONString(systemMsg), WsServerConfig.CHARSET);
					Tio.sendToUser(channelContext.groupContext, mine.getString("id"), response);
					return null;
				}
				GroupService groupService = Aop.get(GroupService.class);
				Groups group = groupService.findById(to.getString("id"));
				Integer groupStatus = group.getGroupStatus();
				if (groupStatus == 0) {
					systemMsg.setSystem(true);
					systemMsg.setId(to.getIntValue("id"));
					systemMsg.setType(chatMsg.getType());
					systemMsg.setMsgtype("message");
					systemMsg.setContent("暂时不允许发言");
					WsResponse response = WsResponse.fromText(JSON.toJSONString(systemMsg), WsServerConfig.CHARSET);
					Tio.sendToUser(channelContext.groupContext, mine.getString("id"), response);
					return null;
				}
				// 发送群消息
				chatMsg.setId(to.getString("id"));
				chatMsg.setMine(false);
				String mineId = mine.getString("id");
				String sendStr = JSON.toJSONString(chatMsg);
				WsResponse response = WsResponse.fromText(sendStr, WsServerConfig.CHARSET);
				// 发送群消息,过滤自己
				Tio.sendToGroup(channelContext.groupContext, to.getString("id"), response, new ChannelContextFilter() {
					@Override
					public boolean filter(ChannelContext channelContext) {
						String userid = channelContext.userid;
						if (userid.equals(mineId)) {
							return false;
						}
						return true;
					}
				});
			} else {
				// 发送者id
				chatMsg.setId(mine.getString("id"));
				// 发送私聊
				String sendStr = JSON.toJSONString(chatMsg);
				WsResponse response = WsResponse.fromText(sendStr, WsServerConfig.CHARSET);
				Tio.sendToUser(channelContext.groupContext, to.getString("id"), response);
			}
		}
		return null;
	}

	/**
	 * 告诉好友你的状态
	 * 
	 * @param user
	 * @param channelContext
	 * @param i
	 */
	public void updateState(Users user, ChannelContext channelContext, int state) {
		UserFriendService userFriendService = Aop.get(UserFriendService.class);
		List<UserFriends> friends = userFriendService.findByUserId(user);
		FriendState friendState = new FriendState();
		friendState.setId(user.getId());
		friendState.setMsgtype("friendState");
		friendState.setName(user.getUserName());
		friendState.setState(state);
		for (UserFriends userFriend : friends) {
			WsResponse wsRes = WsResponse.fromText(JSON.toJSONString(friendState), WsServerConfig.CHARSET);
			Tio.sendToUser(channelContext.groupContext, userFriend.getFriendId().toString(), wsRes);
		}
	}

}
