package kr.mohi.session;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.Server;

public abstract class Session {
	// protected boolean allowsBroadcastMessage = true;
	protected boolean allowsExternalMessage = false;
	protected boolean allowsChatting = true;
	private Set<Player> players = new HashSet<Player>();
	protected String name = "";
	private int id;

	Session() {

	}

	public static void join(Player player, int id) {
		Session.join(player, SessionPlugin.getInstance().getSessions().get(id));
	}

	public static void join(Player player, Session session) {
		if (Session.hasSession(player)) {
			Session.getSession(player).escapeSession(player);
		}
		if (session != null) {
			session.join(player);
			//TODO 이벤트 처리
		}
	}

	public static boolean hasSession(Player player) {
		for (Session session : SessionPlugin.getInstance().getSessions()) {
			if (session.players.contains(player)) {
				return true;
			}
		}
		return false;
	}

	public int newSession(Class<? extends Session> clazz) {
		try {
			Constructor<? extends Session> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			Session session = (Session) constructor.newInstance();
			SessionPlugin.getInstance().getSessions().add(session);
			return SessionPlugin.getInstance().getSessions().indexOf(session);
			//TODO 이벤트 처리
		} catch (Exception e) {
			return -1;
		}
	}

	public static void removeSession(Session session) {
		session.getPlayerSet().forEach(player -> {
			session.escapeSession(player);
		});
		SessionPlugin.getInstance().getSessions().remove(session.getId());
	}

	public static Session getSession(Player player) {
		return SessionPlugin.getInstance().getSessions().stream().filter((session) -> session.players.contains(player))
				.findAny().orElse(null);
	}

	public static void chatMessage(String message) {
		for (Player p : Server.getInstance().getOnlinePlayers().values()) {
			if (!Session.hasSession(p)) {
				p.sendMessage(message);
				//TODO 이벤트 처리
			}
		}
	}

	public void join(Player player) {
		this.players.add(player);
		// TODO 이벤트 처리
	}

	public void escapeSession(Player player) {
		this.players.remove(player);
		//TODO 이벤트 처리
	}

	public void sendMessage(String message) {
		Server.getInstance().broadcastMessage(message, (Player[]) this.players.toArray());
	}

	public String getName() {
		return name;
	}

	/*
	 * public boolean isAllowBroadcastMessage() { return
	 * this.allowsBroadcastMessage; }
	 */

	public boolean isAllowExternalMessage() {
		return this.allowsExternalMessage;
	}

	public boolean isAllowChatting() {
		return this.allowsChatting;
	}

	public Set<Player> getPlayerSet() {
		return this.players;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
