import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by c-consalpa on 12/7/2017.
 */
public class ClientWindow extends JFrame implements TCPConnectionEventListener {
    public static void main(String[] args) {
        new ClientWindow();
    }

    private TCPConnectionEventListener eventListener;
    private TCPConnection tcpConnection;
    private final int PORT = 8189;
    private final String HOST = "127.0.0.1";
    Socket socket;

    private JFrame frame;
    private JTextArea mainTextArea;
    private JTextField usernameField;
    private JTextField input;




    public ClientWindow() {
        buildUI();
        tcpConnection =  establishConnection(HOST, PORT);
        tcpConnection.sendString("aasdw");
        tcpConnection.disconnect(tcpConnection);


    }

    private TCPConnection establishConnection(String host, int port) {
        try {
            return new TCPConnection(HOST, PORT, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendString(String test) {
        tcpConnection.sendString(test);
    }

    void buildUI() {
        frame = new JFrame("SocketChat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);

        mainTextArea = new JTextArea();
        mainTextArea.setEditable(false);
        usernameField = new JTextField();
        input = new JTextField();

        frame.add(usernameField, BorderLayout.NORTH);
        frame.add(mainTextArea, BorderLayout.CENTER);
        frame.add(input, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    @Override
    public void onReceiveMessage(TCPConnection tcpConnection, String message) {

    }

    @Override
    public void onConnectionEstablished(TCPConnection tcpConnection) {
        logEvent("Connected to the server: "+HOST +":"+PORT);
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

    }

    @Override
    public void logEvent(String event) {
        System.out.println("CLIENT: "+ event);
    }


}
