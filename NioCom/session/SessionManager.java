package NioCom.session;

public interface SessionManager {
    public void addSession(Object attachment,Session session) ;
    public void removeSession(Session session) ;
    public Session findSession(Object attachment);
}
