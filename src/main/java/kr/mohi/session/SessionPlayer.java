package kr.mohi.session;

import cn.nukkit.Player;
import cn.nukkit.network.SourceInterface;
import cn.nukkit.network.protocol.TextPacket;

public class SessionPlayer extends Player {

	public SessionPlayer(SourceInterface interfaz, Long clientID, String ip, int port) {
		super(interfaz, clientID, ip, port);
	}

	@Override
	public void sendMessage(String message) {
		if (Session.hasSession(this)) {
			if (!Session.getSession(this).isAllowExternalMessage()) {
				return;
			}
		}
		String[] mes = this.server.getLanguage().translateString(message).split("\\n");
		for (String m : mes) {
			if (!"".equals(m)) {
				TextPacket pk = new TextPacket();
				pk.type = TextPacket.TYPE_RAW;
				pk.message = m;
				this.dataPacket(pk);
			}
		}
	}
}
