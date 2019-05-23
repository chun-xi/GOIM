package ec.mark.im.common.vo;

import java.util.List;

import ec.mark.im.common.model.Groups;
import ec.mark.im.common.model.Users;


public class GroupDetail {
	private List<Users> users;
	private Groups group;
	private String msg;
	private int state;
	public List<Users> getUsers() {
		return users;
	}
	public void setUsers(List<Users> users) {
		this.users = users;
	}
	public Groups getGroup() {
		return group;
	}
	public void setGroup(Groups group) {
		this.group = group;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}



}
