window.onload = function(){
	var find_text = document.getElementById('find_text');
	var find_clear = document.getElementById('find_clear');
	var find_search = document.getElementById('find_search');
	var find_search_group = document.getElementById('find_search_group');
	var users = document.querySelector('.users');
	//判断搜索框是否输入内容
	find_text.oninput = function(){
		if(find_text.value == ''){
			find_clear.style.display = 'none';//如果内容为空，隐藏图标
		}else{
			find_clear.style.display = 'inline-block';//如果输入内容，则显示图标
		}
	}
	//清空内容
	find_clear.onclick = function(){
		find_text.value = '';
		find_clear.style.display = 'none';
	}
	//更改搜索按钮样式
	
	$(find_search).bind('touchstart',function(){
		find_search.style.backgroundColor = '#5a5a5a';			
	})
	$(find_search).bind('touchend',function(){
		find_search.style.backgroundColor = '#36373C';			
	})	
	$(find_search_group).bind('touchstart',function(){
		find_search_group.style.backgroundColor = '#5a5a5a';			
	})
	$(find_search_group).bind('touchend',function(){
		find_search_group.style.backgroundColor = '#36373C';			
	})	
	
	
	//点击搜索
	find_search.onclick = function(){
        var user=$("#find_text").val();
        if(user==''){ return;}
		//清空users
		//users.innerHTML = '无';

        $.ajax({
            url:'/serchFriend',
            data:{userParas:user},
            type:'post',
            dataType:'json',
            success:function(opResult){
                    //遍历数据 添加
                    if(opResult==null||opResult.length==0){
                    	layer.msg("未找到该用户!");
                        return;
                    }
                    var html="";
                    for(var i=0;i<opResult.length;i++){
                       html+="<li class='user'>" +
                           "<div class='user_box'> <img src='"+opResult[i].user_avatar+"' alt='' class='user_head'/>" +
                        "<span class='user_name'>"+opResult[i].user_name+"</span>" +
                        " <span class='user_ana'>"+opResult[i].user_sign+"</span>" +
                        "<span class='user_add' onclick=\"add_user('"+opResult[i].id+"',this)\" >添加</span> </div></li>";
                    }
                    //alert(html);
                    $(".users").html(html);
            }
        });
	}
	//点击搜索
	find_search_group.onclick = function(){
        var group=$("#find_text").val();
        if(group==''){ return;}
		//清空users
		//users.innerHTML = '无';

        $.ajax({
            url:'/serchGroup',
            data:{groupParas:group},
            type:'post',
            dataType:'json',
            success:function(opResult){
                    //遍历数据 添加
                    if(opResult==null||opResult.length==0){
                    	layer.msg("未找到该群!");
                        return;
                    }
                    var html="";
                    for(var i=0;i<opResult.length;i++){
                       html+="<li class='user'>" +
                           "<div class='user_box'> <img src='"+opResult[i].group_avatar+"' alt='' class='user_head'/>" +
                        "<span class='user_name'>"+opResult[i].group_name+"</span>" +
                        " <span class='user_ana'>"+opResult[i].group_sign+"</span>" +
                        "<span class='user_add' onclick=\"add_group('"+opResult[i].id+"',this)\" >加群</span> </div></li>";
                    }
                    //alert(html);
                    $(".users").html(html);
            }
        });
	}
	
}
