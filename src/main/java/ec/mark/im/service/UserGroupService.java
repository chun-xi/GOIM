package ec.mark.im.service;

import java.util.List;

import ec.mark.im.common.model.UserGroup;
import ec.mark.im.common.model.Users;
import io.jboot.service.JbootServiceBase;

public class UserGroupService extends JbootServiceBase<UserGroup> {
	/**
	 * 获取用户分组
	 * 
	 * @param user
	 * @return
	 */
	public List<UserGroup> getUserGroups(Users user) {
		return DAO.findListByColumn("user_id", user.getId());
	}

}
