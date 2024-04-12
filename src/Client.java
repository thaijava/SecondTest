import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    String hostName = "localhost";
    int port = 8228;

    Socket socketToHost;
    public Client() throws IOException {
        socketToHost = new Socket(hostName, port);

    }

    public  void registerPlayer() {
    }
}
