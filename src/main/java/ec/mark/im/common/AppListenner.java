package ec.mark.im.common;

import com.jfinal.config.Constants;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Routes;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.template.Engine;

import ec.mark.im.ws.IMServerStarter;
import io.jboot.aop.jfinal.JfinalHandlers;
import io.jboot.aop.jfinal.JfinalPlugins;
import io.jboot.core.listener.JbootAppListener;
import io.jboot.db.JbootDbManager;
import io.jboot.web.fixedinterceptor.FixedInterceptors;

public class AppListenner implements JbootAppListener {

	@Override
	public void onInit() {

	}

	@Override
	public void onConstantConfig(Constants constants) {
		constants.setDevMode(true);
	}

	@Override
	public void onRouteConfig(Routes routes) {

	}

	@Override
	public void onEngineConfig(Engine engine) {

	}

	@Override
	public void onPluginConfig(JfinalPlugins plugins) {
		plugins.add(new IMServerStarter());
		ActiveRecordPlugin activeRecordPlugin = JbootDbManager.me().getActiveRecordPlugins().get(0);
		activeRecordPlugin.setShowSql(false);
	}

	@Override
	public void onInterceptorConfig(Interceptors interceptors) {
		interceptors.add(new SessionInViewInterceptor());
	}

	@Override
	public void onFixedInterceptorConfig(FixedInterceptors fixedInterceptors) {

	}

	@Override
	public void onHandlerConfig(JfinalHandlers handlers) {

	}

	@Override
	public void onStartBefore() {

	}

	@Override
	public void onStart() {

	}

	@Override
	public void onStop() {

	}

}
