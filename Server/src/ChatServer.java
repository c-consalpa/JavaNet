import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * Created by Varvara on 06.12.2017.
 */
public class ChatServer implements TCPConnectionEventListener {
    public static void main(String[] args) {
        new ChatServer();
    }

    private ServerSocket serverSocket;
    private ArrayList<TCPConnection> activeConnections = new ArrayList<>();

    private ChatServer() {
        try {
            serverSocket = new ServerSocket(8189);
            System.out.println("Server started");
            while (true) {
                try {
                    new TCPConnection(serverSocket.accept(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("Cannto close socket");
                e.printStackTrace();
            }
        }
    }


    @Override
    public synchronized void onReceiveMessage(TCPConnection tcpConnection, String message) {
        String tmp = "received : \""+ message+"\" from: "+tcpConnection+" Thread: "+Thread.currentThread().getName();
        System.out.println(tmp);
        responseToAll(message);
    }

    @Override
    public void onConnectionEstablished(TCPConnection tcpConnection) {
        activeConnections.add(tcpConnection);
        logEvent("Client connected: "+ tcpConnection + ", active: "+activeConnections.size());
    }

    @Override
    public void onError(Exception e) {
        logEvent("error occured: "+e);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        activeConnections.remove(tcpConnection);
        logEvent("Current active connections: "+ activeConnections.size());
    }

    @Override
    public void logEvent(String event) {
        System.out.println(event);
    }

    private void responseToAll(String line) {
        for (int i = 0; i < activeConnections.size(); i++) {
            activeConnections.get(i).sendString(line);
        }
    }
}
