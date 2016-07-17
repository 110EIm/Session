package kr.mohi.session.event;

import kr.mohi.session.Session;

/**
 * @since 2016-07-16
 * @author 110EIm
 */
public class SessionRemoveEvent extends SessionEvent{
    public SessionRemoveEvent(Session session) {
        super(session);
    }
}
