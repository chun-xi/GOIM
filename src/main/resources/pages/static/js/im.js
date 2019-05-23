function setGroupStatus(status,group, obj) {
	$.post('/user/groupSetState', {groupId: group,state:status==1?0:1}, function(res){
		if (res.state=="ok") {
			obj.innerHTML = "修改成功";
		}
	});
}

function delGroupUser(user,group,obj){
	$.post('/user/delGroupUser', {userId: user,groupId:group}, function(res){
		if (res.state=="ok") {
			obj.innerHTML = "删除成功";
		}else {
			layer.msg(res.msg);
		}
	});
}
function delUser(user, obj) {
	$.post('/user/delUser', {userId: user}, function(res){
		if (res.state=="ok") {
			obj.innerHTML = "删除成功";
		}
	});
}