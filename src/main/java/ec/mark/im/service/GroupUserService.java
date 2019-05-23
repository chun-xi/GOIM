package ec.mark.im.service;


import com.jfinal.plugin.activerecord.Db;

import ec.mark.im.common.model.GroupUsers;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;

public class GroupUserService extends JbootServiceBase<GroupUsers> {
	/**
	 * 删除狗群员
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public int delGroupUser(Long userId, Long groupId) {
		return Db.delete("delete from group_user where user_id=? and group_id=?", userId, groupId);
	}

	/**
	 * 用户是否再群里
	 * 
	 * @param userId
	 * @param groupId
	 * @return
	 */
	public boolean isInGroup(Long userId, Long groupId) {
		Columns columns = Columns.create().add("user_id", userId).add("group_id", groupId);
		GroupUsers user = DAO.findFirstByColumns(columns);
		if (user == null) {
			return true;
		}
		return false;
	}
}
