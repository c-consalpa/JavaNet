import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Created by c-consalpa on 12/7/2017.
 */
public class ClientWindow extends JFrame implements TCPConnectionEventListener, ActionListener {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private TCPConnectionEventListener eventListener;
    private TCPConnection tcpConnection;
    private final int PORT = 8189;
    private final String HOST = "127.0.0.1";


    private JFrame frame;
    private JTextArea mainTextArea;
    private JTextField usernameField;
    private JTextField input;




    public ClientWindow() {
        buildUI();
        tcpConnection = establishConnection(HOST, PORT);
    }

    private TCPConnection establishConnection(String host, int port) {
        TCPConnection ClientConnection = null;
        try {
            ClientConnection = new TCPConnection(HOST, PORT, this);
            return ClientConnection;
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
        input.addActionListener(this);

        frame.add(usernameField, BorderLayout.NORTH);
        frame.add(mainTextArea, BorderLayout.CENTER);
        frame.add(input, BorderLayout.SOUTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if (tcpConnection!=null) {
                    try {
                        System.out.println("Closing Window");
                        tcpConnection.disconnect();
                        System.exit(1);
                    } catch (IOException e1) {
                        System.out.println("Can't disconnect");
                    }
                }
            }
        });



        frame.setVisible(true);
    }

    @Override
    public void onReceiveMessage(TCPConnection tcpConnection, String message) {
        logEvent("message received from: "+tcpConnection + " -> "+message);
        mainTextArea.append(message);
    }

    @Override
    public void onConnectionEstablished(TCPConnection tcpConnection) {
        System.out.println(tcpConnection);
    }

    @Override
    public void onError(Exception e) {
       logEvent("Error occurred : "+e);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

    }

    @Override
    public void logEvent(String event) {
        System.out.println("CLIENT: "+ event);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tcpConnection.sendString(usernameField.getText()+ " : "+input.getText());
    }
}
