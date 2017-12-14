import java.io.*;
import java.net.Socket;

/**
 * Created by Varvara on 06.12.2017.
 */
public class TCPConnection {
    private Socket socket;
    private Thread rxThread;
    private BufferedReader in;
    private BufferedWriter out;
    private TCPConnectionEventListener TeventListener;



    public TCPConnection(String hostname, int portNumber, TCPConnectionEventListener eventListener) throws IOException {
       this(new Socket(hostname, portNumber), eventListener);
    }

    public TCPConnection(Socket sckt, TCPConnectionEventListener eventListener) {
        this.socket = sckt;
        this.TeventListener = eventListener;
        TeventListener.onConnectionEstablished(this);

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println("Cant initialize streams");
            e.printStackTrace();
        } finally {

        }

        rxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String line="";
                try {
                    while ((line=in.readLine())!=null){
                        if(line.equals("\\q")) {
                            in.close();
                            out.close();
                            socket.close();
                            Thread.currentThread().interrupt();
                            break;
                        }
                        eventListener.onReceiveMessage(TCPConnection.this, line);
                    }
                    System.out.println("EOF reached");

                    TeventListener.onDisconnect(TCPConnection.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        rxThread.start();
    }





    public void disconnect() throws IOException {
        System.out.println("Terminating TCP connection: "+ this);
        out.write("\\q");
        out.newLine();
        out.flush();
        in.close();
        out.close();
        socket.close();

        Thread.currentThread().interrupt();

    }

//    @Override
//    public String toString() {
//        return socket.getInetAddress() + ": " + socket.getPort();
//    }

    public void sendString(String str) {
        try {
            out.write(str);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
