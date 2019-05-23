package ec.mark.im.ws;

import java.io.IOException;

import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;
import com.jfinal.plugin.IPlugin;
import ec.mark.im.ws.handler.IMWsMessageHandler;
import ec.mark.im.ws.listenner.IMSercerAioListenner;

public class IMServerStarter implements IPlugin {
	public static WsServerStarter wsServerStarter;
	public static ServerGroupContext serverGroupContext;
	@Override
	public boolean start() {
		try {
			wsServerStarter = new WsServerStarter(WsServerConfig.SERVER_PORT, IMWsMessageHandler.me);
			serverGroupContext = wsServerStarter.getServerGroupContext();
			serverGroupContext.setName(WsServerConfig.PROTOCOL_NAME);
			serverGroupContext.setServerAioListener(IMSercerAioListenner.me);
			// 设置心跳超时时间
			// serverGroupContext.setHeartbeatTimeout(WsServerConfig.HEARTBEAT_TIMEOUT);
			wsServerStarter.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean stop() {
		return false;
	}

}
