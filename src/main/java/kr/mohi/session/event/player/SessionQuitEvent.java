package kr.mohi.session.event.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import kr.mohi.session.Session;

/**
 * @author 110EIm
 * @since 2016-07-16
 */
public class SessionQuitEvent extends SessionPlayerEvent {
    private String message;

    public SessionQuitEvent(Session session, CommandSender player, String quitMessage) {
        super(session, player);
        this.message = quitMessage;
    }

    public void setQuitMessage(String message) {
        this.message = message;
    }

    public String getQuitMessage() {
        return this.message;
    }
}
