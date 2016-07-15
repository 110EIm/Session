package kr.mohi.session;

import java.lang.reflect.Constructor;
import java.util.LinkedList;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;

public class SessionPlugin extends PluginBase implements Listener {
	LinkedList<Session> sessions = new LinkedList<Session>();
	private static SessionPlugin instance = null;

	@Override
	public void onEnable() {
		SessionPlugin.instance = this;
		Server.getInstance().getPluginManager().registerEvents(this, this);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onChat(PlayerChatEvent event) {
		this.sessions.forEach((session) -> {
			if (!session.isAllowChatting()) {
				event.getPlayer().sendMessage("This session doesn't allow chatting");
				event.setCancelled();
				return;
			}
			if (session.getPlayerSet().contains(event.getPlayer())) {
				String message = Server.getInstance().getLanguage().translateString(event.getFormat(),
						new String[] { event.getPlayer().getDisplayName(), event.getMessage() });
				session.sendMessage(message);
				event.setCancelled();
			}
		});
		if (!event.isCancelled()) {
			Session.chatMessage(Server.getInstance().getLanguage().translateString(event.getFormat(),
					new String[] { event.getPlayer().getDisplayName(), event.getMessage() }));
			event.setCancelled();
		}
	}

	public int newSession(Class<? extends Session> clazz) {
		try {
			Constructor<? extends Session> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			Session session = (Session) constructor.newInstance();
			this.getSessions().add(session);
			return this.getSessions().indexOf(session);
		} catch (Exception e) {
			return -1;
		}
	}
	
	public void removeSession(Session session) {
		session.getPlayerSet().forEach(player -> {
			session.escapeSession(player);
		});
		this.sessions.remove(session.getId());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if (Session.hasSession(event.getPlayer())) {
			Session.getSession(event.getPlayer()).getPlayerSet().remove(event.getPlayer());
		}
	}

	public static SessionPlugin getInstance() {
		return SessionPlugin.instance;
	}

	public LinkedList<Session> getSessions() {
		return this.sessions;
	}
}
