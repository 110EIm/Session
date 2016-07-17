package kr.mohi.session.event.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import kr.mohi.session.Session;

/**
 * @author 110EIm
 * @since 2016-07-16
 */
public class SessionJoinEvent extends SessionPlayerEvent {
    private String message;

    public SessionJoinEvent(Session session, CommandSender player, String joinMessage) {
        super(session, player);
        this.message = joinMessage;
    }

    public void setJoinMessage(String message) {
        this.message = message;
    }

    public String getJoinMessage() {
        return this.message;
    }
}
