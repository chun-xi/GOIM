package ec.mark.im.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.aop.Inject;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.Ret;
import com.jfinal.upload.UploadFile;

import ec.mark.im.common.model.GroupUsers;
import ec.mark.im.common.model.Groups;
import ec.mark.im.common.model.UserFriends;
import ec.mark.im.common.model.UserGroup;
import ec.mark.im.common.model.Users;
import ec.mark.im.common.vo.FriendVo;
import ec.mark.im.common.vo.GroupDetail;
import ec.mark.im.common.vo.GroupVo;
import ec.mark.im.common.vo.LayData;
import ec.mark.im.common.vo.SystemMsg;
import ec.mark.im.common.vo.UserVo;
import ec.mark.im.interceptor.UserInterceptor;
import ec.mark.im.service.GroupService;
import ec.mark.im.service.GroupUserService;
import ec.mark.im.service.UserFriendService;
import ec.mark.im.service.UserGroupService;
import ec.mark.im.service.UserService;
import ec.mark.im.ws.IMServerStarter;
import ec.mark.im.ws.WsServerConfig;
import io.jboot.web.controller.JbootController;
import io.jboot.web.controller.annotation.RequestMapping;

@RequestMapping("/")
@Before(UserInterceptor.class)
public class UserController extends JbootController {
	@Inject
	UserService userService;
	@Inject
	UserGroupService userGroupService;
	@Inject
	GroupService groupService;
	@Inject
	UserFriendService userFriendService;
	@Inject
	GroupUserService groupUserService;

	public void index() {
		Users login_user = getSessionAttr("user");
		// 获取分组与好友
		List<UserGroup> userGroups = userGroupService.getUserGroups(login_user);
		List<FriendVo> friendVos = new ArrayList<>();
		for (UserGroup userGroup : userGroups) {
			// 获取分组下的好友
			FriendVo friendVo = new FriendVo();
			friendVo.setGroupname(userGroup.getUserGroupName());
			friendVo.setId(userGroup.getId());
			List<Users> users = userService.findByGroup(userGroup);
			List<UserVo> userVos = new ArrayList<>();
			for (Users user : users) {
				UserVo userVo = new UserVo();
				userVo.setId(user.getId());
				userVo.setUsername(user.getUserName());
				userVo.setAvatar(user.getUserAvatar());
				userVo.setStatus(user.getUserState() == 1 ? "online" : "offline");
				userVo.setSign(user.getUserSign());
				userVos.add(userVo);
			}
			friendVo.setList(userVos);
			friendVos.add(friendVo);
		}
		// 获取群列表
		List<Groups> myGroups = groupService.getMyGroups(login_user);
		List<GroupVo> groupVos = new ArrayList<>();
		for (Groups group : myGroups) {
			GroupVo groupVo = new GroupVo();
			groupVo.setId(group.getId());
			groupVo.setGroupname(group.getGroupName());
			groupVo.setAvatar(group.getGroupAvatar());
			groupVos.add(groupVo);

		}
		setAttr("friend", JSON.toJSONString(friendVos));
		setAttr("group", JSON.toJSONString(groupVos));
		if (isMobileBrowser()) {
			render("mobile.html");
			return;
		}
		render("pc.html");
	}

	/**
	 * 获取狗群员
	 * 
	 * @param id
	 */
	public void getMembers(Long id) {
		LayData data = new LayData();
		List<Users> users = userService.getGroupUsers(id);
		Map<String, List<UserVo>> m = new HashMap<>();
		List<UserVo> list = new ArrayList<UserVo>();
		for (Users user : users) {
			UserVo userVo = new UserVo();
			userVo.setId(user.getId());
			userVo.setAvatar(user.getUserAvatar());
			userVo.setUsername(user.getUserName());
			userVo.setSign(user.getUserSign());
			list.add(userVo);
		}
		m.put("list", list);
		data.setCode(0);
		data.setMsg("");
		data.setData(m);
		renderJson(data);
	}

	/**
	 * 用户信息页面
	 */
	public void info() {
		Users user = getSessionAttr("user");
		// 获取最新资料
		Users user2 = userService.findById(user.getId());
		setAttr("user", user2);
		render("/info.html");
	}

	/**
	 * 更新用户
	 */
	public void updateUser() {
		Users user = getSessionAttr("user");
		Users model = getModel(Users.class, "");
		model.setId(user.getId());
		model.setUpdateTime(new Date());
		model.update();
		renderJson(Ret.ok("msg", "成功"));
	}

	/**
	 * 查看所有狗群友
	 * 
	 * @param groupId
	 */
	public void groupDetail(Long groupId) {
		GroupDetail gDetail = new GroupDetail();
		List<Users> users = userService.getGroupUsers(groupId);
		Groups group = groupService.findById(groupId);
		gDetail.setGroup(group);
		gDetail.setUsers(users);
		gDetail.setState(1);
		renderJson(gDetail);
	}

	// 聊天图片上传
	public void uploadImg() {
		UploadFile uploadFile = getFile();
		File file = uploadFile.getFile();
		String name = file.getName();
		if (getPara("type", "upload").equals("head")) {
			Users user = getSessionAttr("user");
			user.setUserAvatar("/upload/" + name);
			user.update();
			setSessionAttr("user", user);
		}
		renderJson("{\"code\": 0,\"msg\":\"成功\",\"data\": {\"src\":\"/upload/" + name + "\"}}");
	}

	public void addFriend(Long friendId) {
		Users loginUser = getSessionAttr("user");
		Users user = userService.findById(friendId);
		// 如果被加对象是VIP 或者 登录用户是VIP才可加人
		if (user.getIsVip() != 1 && loginUser.getIsVip() != 1) {
			renderJson(Ret.fail("msg", "只允许添加客服好友"));
			return;
		}
		// 添加好友
		UserFriends userFriend = new UserFriends();
		userFriend.setFriendId(friendId);
		userFriend.setUserId(loginUser.getId());
		// 默认放在第一个分组
		List<UserGroup> userGroups = userGroupService.getUserGroups(loginUser);
		UserGroup group = userGroups.get(0);
		userFriend.setUserGroupId(group.getId());
		userFriend.setCreateTime(new Date());
		userFriendService.save(userFriend);
		// 被动添加
		UserFriends userFriend1 = new UserFriends();
		userFriend1.setFriendId(loginUser.getId());
		userFriend1.setUserId(friendId);
		// 默认放在第一个分组
		Users user1 = new Users();
		user1.setId(friendId);
		List<UserGroup> userGroups1 = userGroupService.getUserGroups(user1);
		UserGroup group1 = userGroups1.get(0);
		userFriend1.setUserGroupId(group1.getId());
		userFriend1.setCreateTime(new Date());
		userFriendService.save(userFriend1);
		renderJson(Ret.ok());
	}

	/**
	 * 加群
	 * 
	 * @param groupId
	 */
	public void addGroup(Long groupId) {
		Users loginUser = getSessionAttr("user");
		GroupUsers groupUser = new GroupUsers();
		groupUser.setGroupId(groupId);
		groupUser.setUserId(loginUser.getId());
		groupUser.setJoinTime(new Date());
		groupUserService.save(groupUser);
		SystemMsg systemMsg = new SystemMsg();
		systemMsg.setSystem(true);
		systemMsg.setId(groupId.intValue());
		systemMsg.setType("group");
		systemMsg.setMsgtype("message");
		systemMsg.setContent(String.format("用户:%s,加入群组", loginUser.getUserName()));
		WsResponse response = WsResponse.fromText(JSON.toJSONString(systemMsg), WsServerConfig.CHARSET);
		Tio.sendToGroup(IMServerStarter.serverGroupContext, groupId.toString(), response);

		renderJson(Ret.ok());
	}

	/**
	 * 查找用户
	 * 
	 * @param userParas
	 */
	public void serchFriend(String userParas) {
		List<Users> users = userService.getUserListByNameOrPhone(userParas);
		renderJson(users);
	}

	/**
	 * 查找群
	 * 
	 * @param groupParas
	 */
	public void serchGroup(String groupParas) {
		List<Groups> groups = groupService.getGroupListByName(groupParas);
		renderJson(groups);
	}

	/**
	 * 登录
	 * 
	 * @param username
	 * @param password
	 */
	@Clear
	public void login(String username, String password) {
		Users user = userService.getUserByPhone(username);
		if (user != null) {
			if (user.getUserPwd().equalsIgnoreCase(HashKit.md5(password))) {
				setSessionAttr("user", user);
				renderJson(Ret.ok());
				return;
			}
		}
		renderJson(Ret.fail("msg", "账户或密码错误"));
	}
}