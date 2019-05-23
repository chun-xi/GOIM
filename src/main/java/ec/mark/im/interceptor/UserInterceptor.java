package ec.mark.im.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import ec.mark.im.common.model.Users;

public class UserInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		Users user = c.getSessionAttr("user");
		if (user == null) {
			c.render("login.html");
			return;
		}
		inv.invoke();
	}

}
