package ec.mark.im.service;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import ec.mark.im.common.model.Groups;
import ec.mark.im.common.model.Users;
import ec.mark.im.common.vo.LayData;
import io.jboot.db.model.Column;
import io.jboot.db.model.Columns;
import io.jboot.service.JbootServiceBase;

public class GroupService extends JbootServiceBase<Groups> {
	/**
	 * 获取用户群列表
	 * 
	 * @param user
	 * @return
	 */
	public List<Groups> getMyGroups(Users user) {
		return DAO.find("SELECT t2.* FROM group_users t1,`groups` t2  WHERE t1.group_id = t2.id AND t1.user_id = ?",
				user.getId());
	}

	/**
	 * 获取用户群列表[page]
	 * 
	 * @param user
	 * @return
	 */
	public LayData getMyGroups(int pageNumber, int pageSize, Users user) {
		String select = "SELECT t2.*";
		String sqlExceptSelect = "FROM group_users t1,`groups` t2  WHERE t1.group_id = t2.id AND t1.user_id = ?";
		Page<Groups> groups = DAO.paginate(pageNumber, pageSize, select, sqlExceptSelect, user.getId());
		Integer total = Db.queryInt("select count(*) from group_users where user_id=?", user.getId());
		LayData data = new LayData();
		data.setCode(0);
		data.setCount(total);
		data.setData(groups.getList());
		data.setMsg("success");
		return data;
	}

	/**
	 * 获取我的群列表[page]
	 * 
	 * @param user
	 * @return
	 */
	public Page<Groups> getMyGroupList(int pageNumber, int pageSize/* , User user */) {
		return DAO.paginate(pageNumber, pageSize);
	}

	/**
	 * 获取我的群列表
	 * 
	 * @param user
	 * @return
	 */
	public List<Groups> getMyGroupList(Users user) {
		return DAO.findListByColumn(Column.create("user_id", user.getId()));
	}

	/**
	 * 获取群列表
	 * 
	 * @param group
	 */
	public List<Groups> getGroupListByName(String group) {
		Columns cols = new Columns();
		cols.add("group_name", group).or().add("id", group);
		return DAO.findListByColumns(cols);
	}

}
