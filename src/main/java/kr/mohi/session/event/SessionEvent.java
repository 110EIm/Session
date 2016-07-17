package kr.mohi.session.event;

import cn.nukkit.event.Event;
import kr.mohi.session.Session;

/**
 * @author 110EIm
 * @since 2016.7.16
 */
public class SessionEvent extends Event {
    protected Session session;

    public SessionEvent(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return this.session;
    }
}
