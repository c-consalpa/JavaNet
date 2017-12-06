/**
 * Created by Varvara on 07.12.2017.
 */
public interface TCPConnectionEventListener {
    void onReceiveMessage(TCPConnection tcpConnection, String message);
    void onConnectionEstablished(TCPConnection tcpConnection);
    void onError(Exception e);
    void onDisconnect(TCPConnection tcpConnection);
    void logEvent(String event);
}
