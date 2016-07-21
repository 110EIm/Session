package kr.mohi.session;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * @author 110EIm
 * @since 2016-7-15
 */
public class SessionPlugin extends PluginBase implements Listener {
    static LinkedList<Session> sessions = new LinkedList<>();
    private static SessionPlugin instance = null;

    static SessionPlugin getInstance() {
        return SessionPlugin.instance;
    }

    @Override
    public void onEnable() {
        SessionPlugin.instance = this;
        Server.getInstance().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();
        String format = event.getFormat();

        if (!Session.hasSession(player)) {
            event.setRecipients(Server.getInstance().getOnlinePlayers().values().stream().filter(p -> !Session.hasSession(p)).collect(Collectors.toSet()));
            return;
        }
        Session session = Session.getSession(player);
        session.chatMessage(player, message, format);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (Session.hasSession(event.getPlayer())) {
            Session.getSession(event.getPlayer()).getPlayerSet().remove(event.getPlayer());
        }
    }
}
