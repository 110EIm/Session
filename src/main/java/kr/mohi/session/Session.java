package kr.mohi.session;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import kr.mohi.session.event.player.SessionChatEvent;
import kr.mohi.session.event.player.SessionJoinEvent;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author 110EIm
 * @since 2016.7.15
 */
public abstract class Session {
    // protected boolean allowsBroadcastMessage = true;
    protected boolean allowsExternalMessage = false;
    protected boolean allowsChatting = true;
    protected String name = "";
    protected String joinMessgae = "Welcome to " + name;
    protected String quitMessage = "Good bye!";
    private Set<CommandSender> players = new HashSet<>();
    private int id;

    Session() {

    }

    public static void join(Player player, int id) {
        Session.join(player, Session.getSessions().get(id));
    }

    public static void join(Player player, Session session) {
        if (Session.hasSession(player)) {
            Session.getSession(player).quit(player);
        }
        if (session != null) {
            session.join(player);

        }
    }

    public static boolean hasSession(Player player) {
        for (Session session : Session.getSessions()) {
            if (session.players.contains(player)) {
                return true;
            }
        }
        return false;
    }

    public static void removeSession(Session session) {
        session.getPlayerSet().forEach(player -> {
            session.quit(player);
        });
        Session.getSessions().remove(session.getId());
    }

    public static Session getSession(Player player) {
        return Session.getSessions().stream().filter((session) -> session.getPlayerSet().contains(player))
                .findAny().orElse(null);
    }

    public static LinkedList<Session> getSessions() {
        return SessionPlugin.sessions;
    }

    public int newSession(Class<? extends Session> clazz) {
        try {
            Constructor<? extends Session> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Session session = (Session) constructor.newInstance();
            Session.getSessions().add(session);
            return Session.getSessions().indexOf(session);
            //TODO 이벤트 처리
        } catch (Exception e) {
            return -1;
        }
    }

    public void join(CommandSender player) {
        SessionJoinEvent event = new SessionJoinEvent(this, player, this.joinMessgae);
        Server.getInstance().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            player.sendMessage(event.getJoinMessage());
            this.players.add(player);
        }
    }

    public void quit(CommandSender player) {
        this.players.remove(player);
        //TODO 이벤트 처리
    }

    public void sendMessage(String message) {
        Server.getInstance().broadcastMessage(message, (Player[]) this.players.toArray());
    }

    public void chatMessage(CommandSender player, String message, String format) {
        if(! this.isAllowChatting()) {
            player.sendMessage("This session does not allow chatting");
            return;
        }
        SessionChatEvent event = new SessionChatEvent(this, player, message, format);
        Server.getInstance().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            player.sendMessage(Server.getInstance().getLanguage().translateString(event.getFormat(),
                    new String[]{((Player)event.getPlayer()).getDisplayName(), event.getMessage()}));
        }
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

    public Set<CommandSender> getPlayerSet() {
        return this.players;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
