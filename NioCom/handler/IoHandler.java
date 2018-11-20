package NioCom.handler;

import java.nio.channels.SelectionKey;

public interface IoHandler {
    public void handle(SelectionKey key) ;
}
