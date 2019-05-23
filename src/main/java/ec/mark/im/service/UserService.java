package ec.mark.im.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import ec.mark.im.common.model.UserGroup;
import ec.mark.im.common.model.Users;
import ec.mark.im.common.vo.LayData;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;

public class UserService extends JbootServiceBase<Users> {

	/**
	 * 获取用户
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Users getUserByNameOrPhone(String username) {
		// 查询参数
		Columns cols = new Columns();
		cols.add("user_name", username).or().add("user_phone", username);
		// 查询用户
		return DAO.findFirstByColumns(cols);
	}

	public Users getUserByPhone(String phone) {
		return DAO.findFirstByColumns(Columns.create().add("user_phone", phone));
	}

	public List<Users> getUserListByNameOrPhone(String username) {
		// 查询参数
		Columns cols = new Columns();
		cols.add("user_name", username).or().add("user_phone", username);
		// 查询用户
		return DAO.findListByColumns(cols);
	}

	/**
	 * 获取分组用户
	 * 
	 * @param userGroup
	 * @return
	 */
	public List<Users> findByGroup(UserGroup userGroup) {
		return DAO.find(
				"select t2.* from user_friend t1,user t2 where t1.friend_id = t2.id and t1.user_id = ? and t1.user_group_id = ?",
				userGroup.getUserId(), userGroup.getId());
	}

	/**
	 * 获取所有好友
	 * 
	 * @param userId
	 * @return
	 */
	public List<Users> findByUser(Long userId) {
		return DAO.find("select t2.* from user_friend t1,user t2 where t1.friend_id = t2.id and t1.user_id = ?",
				userId);
	}

	/**
	 * 查找所有好友[page]
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public LayData findByUser(int pageNumber, int pageSize, int userId) {
		String totalRowSql = "select count(*) from user_friend where user_id=?";
		Integer total = Db.queryInt(totalRowSql, userId);
		String findSql = "from user_friend t1,user t2 where t1.friend_id = t2.id and t1.user_id = ?";
		Page<Users> users = DAO.paginate(pageNumber, pageSize, "select t2.*", findSql, userId);
		LayData data = new LayData();
		data.setCode(0);
		data.setCount(total);
		data.setData(users.getList());
		data.setMsg("success");
		return data;
	}
	/**
	 * 更新用户状态
	 * 
	 * @param userid
	 * @param i
	 */
	public void updateUserState(String userid, int i) {
		Users user = new Users();
		user.setId(Long.valueOf(userid));
		user.setUserState(i);
		update(user);
	}

	/**
	 * 获取群内用户
	 * 
	 * @param groupId
	 * @return
	 */
	public List<Users> getGroupUsers(Long groupId) {
		return DAO.find(
				"SELECT u.user_name,u.user_sign,u.id,u.user_avatar FROM group_user gu,user u WHERE gu.user_id=u.id AND gu.group_id =?",
				groupId);
	}

	/**
	 * 获取群内用户[page]
	 * 
	 * @param groupId
	 * @return
	 */
	public LayData getGroupUsers(int pageNumber, int pageSize, Long groupId) {
		String totalRowSql = "select count(*) from group_user where group_id=?";
		Integer total = Db.queryInt(totalRowSql, groupId);
		String select = "SELECT u.user_name,u.user_sign,u.id,u.user_avatar,gu.join_time ";
		String sqlExceptSelect = "FROM group_user gu,user u WHERE gu.user_id=u.id AND gu.group_id =?";
		Page<Users> users = DAO.paginate(pageNumber, pageSize, select, sqlExceptSelect, groupId);
		LayData data = new LayData();
		data.setCode(0);
		data.setCount(total);
		data.setData(users.getList());
		data.setMsg("success");
		return data;
	}

	/**
	 * 删除关于这个吊毛的所有信息
	 * 
	 * @param userId
	 */
	public void delUser(Long userId) {
		// 删除用户关系
		Db.delete("delete from user_friend where user_id=? or friend_id=?", userId, userId);
		// 删除用户加的装逼群
		Db.delete("delete from group_user where user_id=?", userId);
		deleteById(userId);
	}

	/**
	 * 获取傻逼客服
	 * 
	 * @return
	 */
	public List<Users> getVipList() {
		return DAO.findListByColumn("is_vip", "1");
	}
}
