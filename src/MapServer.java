
import java.io.*;
import java.net.*;



public class MapServer {
    int port = 8088;
    GameMap map = new GameMap();
    public MapServer() {

    }

    public MapServer(int port) {
        this.port = port;
    }

    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started. Listening on port 8080...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                Thread thread = new Thread(new ClientHandler(clientSocket, map));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        MapServer server = new MapServer();
        server.start();
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    GameMap map;

    public ClientHandler(Socket clientSocket, GameMap map) {
        this.clientSocket = clientSocket;
        this.map = map;
    }

    @Override
    public void run() {

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String clientCommand = in.readLine();

            if(clientCommand.equals("REQ MAP DATA")) {

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.writeObject(map.getMapData());
                out.flush();

            }else {
                System.out.println("MAP SERVER: UNKNOWN COMMAND, " + clientCommand);
            }

        } catch (IOException e) {
            System.out.println("MAPSERVER: HANDLE CLIENT COMMAND ERROR:");
            e.printStackTrace();
        }
    }
}
