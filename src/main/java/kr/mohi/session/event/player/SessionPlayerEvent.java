package kr.mohi.session.event.player;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import kr.mohi.session.Session;
import kr.mohi.session.event.SessionEvent;


/**
 * @author 110EIm
 * @since 2016-07-16
 */
public class SessionPlayerEvent extends SessionEvent {
    protected CommandSender player;

    public SessionPlayerEvent(Session session, CommandSender player) {
        super(session);
        this.player = player;
    }

    public CommandSender getPlayer() {
        return this.player;
    }
}
