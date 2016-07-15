package kr.mohi.session;

import java.util.HashSet;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.Server;

public abstract class Session {
	protected boolean allowsBroadcastMessage = true;
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

	public static Session getSession(Player player) {
		return SessionPlugin.getInstance().getSessions().stream().filter((session) -> session.players.contains(player))
				.findAny().orElse(null);
	}

	public String getName() {
		return name;
	}

	// 다른 곳으로 옮기자(Server클래스 바꿔치기)
	public static void broadcastMessage(String message) {
		Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
			if (Session.hasSession(player)) {
				Session.getSession(player).sendMessage(message);
			} else {
				player.sendMessage(message);
			}
		});
		SessionPlugin.getInstance().sessions.forEach(session -> {
			if (session.isAllowBroadcastMessage()) {
				session.sendMessage(message);
			}
		});
	}

	// 이거 다른데로 옮기자(Player클래스 바꿔치기하자)
	public static void sendMessage(Player player, String message) {
		if (!Session.hasSession(player)) {
			return;
		}
		if (Session.getSession(player).isAllowBroadcastMessage()) {
			player.sendMessage(message);
		}
	}

	public static void chatMessage(String message) {
		for (Player p : Server.getInstance().getOnlinePlayers().values()) {
			if (!Session.hasSession(p)) {
				p.sendMessage(message);
			}
		}

	} 

	public void escapeSession(Player player) {
		this.players.remove(player);
	}

	public void join(Player player) {
		this.players.add(player);
		this.onJoin(player);
	}

	public void sendMessage(String message) {
		Server.getInstance().broadcastMessage(message, (Player[]) this.players.toArray());
	}

	public abstract void onJoin(Player player);

	public boolean isAllowBroadcastMessage() {
		return this.allowsBroadcastMessage;
	}

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
