package ec.mark.im.common.vo;

import java.util.List;

public class FriendVo {
	private String groupname;
	private Long id;
	private List<UserVo> list;

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<UserVo> getList() {
		return list;
	}

	public void setList(List<UserVo> list) {
		this.list = list;
	}

}
