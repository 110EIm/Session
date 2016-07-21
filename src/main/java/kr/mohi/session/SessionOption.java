package kr.mohi.session;

/**
 * @author 110EIm
 * @since 2016-07-21
 */
public class SessionOption {
    protected boolean allowChat;

    public SessionOption() {

    }

    public boolean isAllowChat() {
        return this.allowChat;
    }

    public void setAllowChat(boolean set) {
        this.allowChat = set;
    }
}
