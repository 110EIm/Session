package kr.mohi.session.event;

import kr.mohi.session.Session;

/**
 * @author 110EIm
 * @since 2016-07-16
 */
public class SessionCreationEvent extends SessionEvent {
    public SessionCreationEvent(Session session) {
        super(session);
    }
}
