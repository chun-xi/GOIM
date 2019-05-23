package ec.mark.im.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;

import ec.mark.im.common.model.UserFriends;
import ec.mark.im.common.model.Users;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;

public class UserFriendService extends JbootServiceBase<UserFriends> {
	/**
	 * g根据userId获取在线好友
	 * 
	 * @param user
	 * @return
	 */

	public List<UserFriends> findByUserId(Users user) {
		Columns c = new Columns();
		c.add("user_id", user.getId());
		return DAO.findListByColumns(c);
	}

	/**
	 * 删除傻逼好友
	 * 
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public int delUser(long userId, long friendId) {
		int delete = Db.delete("delete from user_friend where user_id=? and friend_id=?", userId, friendId);
		return delete;
	}

}
