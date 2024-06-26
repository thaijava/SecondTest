
import java.io.*;
import java.net.*;
import java.util.*;


public class MapServer extends Thread {
    static final String COMMAND_REQ_MAP = "REQ_MAP:";
    static final String COMMAND_REG_CAR = "REG_CAR:";
    static final String COMMAND_MOVE_TO = "MOVE_TO:";
    static final String COMMAND_GET_FRIENDS = "GET_FRIENDS:";
    String hostIP = "localhost";
    int port = 8225;
    Socket socketToHost;

    ObjectOutputStream ooOut;
    ObjectInputStream ooIn;
    GameMap gameMap;

    boolean isOnline = false;

    Collection<CommandProcessor> carList = new ArrayList<CommandProcessor>();

    public MapServer(GameMap gameMap, int port) throws IOException {
        this.gameMap = gameMap;
        this.port = port;

    }

    public void startAccept() {

        this.start();
        isOnline = true;
    }


    public void connectRemote(String host, int port) throws IOException, ClassNotFoundException {
      //  socketToHost.close();

        isOnline = false;
        socketToHost = new Socket(hostIP, port);
        ooOut = new ObjectOutputStream(socketToHost.getOutputStream());

 //       ooIn = new ObjectInputStream(socketToHost.getInputStream());
 //       Command fromHost = (Command) ooIn.readObject();
 //       int[][] ret = (int[][]) fromHost.p1;

        isOnline = true;

    }

    public GameMap getGameMapObject() {
        return gameMap;
    }

    public void run() {                                     /////////////////        run() of MapServer

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(">>>> GAME SERVER STARTED..." + port);
            socketToHost = new Socket(hostIP, port);
            ooOut = new ObjectOutputStream(socketToHost.getOutputStream());

            System.out.println(">>>> success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                System.out.println(">>>> GAMESERVER HANDLE CONNECTION: " + clientSocket);

                CommandProcessor ccc = new CommandProcessor(clientSocket, this);
                ccc.start();

                carList.removeIf(e -> e.isActive == false);
                System.gc();

                System.out.println("\n Active processor:" + carList.size());

            } catch (Exception e) {
                System.out.println(">>>> MAPSERVER:  LOOP ACCEPT CONNECTION ERROR.");
                e.printStackTrace();
            }
        }

    }

    public String getHost() {
        return hostIP;
    }

    public void carMoveTo(long id, int row, int column) throws IOException {

        while (socketToHost == null) {                                // wait until socket ready
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        ooOut.writeObject(new Command(COMMAND_MOVE_TO,  id, row, column));
    }

    public Command registerCar() throws IOException, ClassNotFoundException{

        while (socketToHost == null) {                                // wait until socket ready
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        ooOut.writeObject(new Command(COMMAND_REG_CAR, null, null, null));
        ooIn = new ObjectInputStream(socketToHost.getInputStream());
        Command retCommand = (Command) ooIn.readObject();

        return retCommand;
    }

    public void isOnline(boolean state) {
        isOnline = state;
    }

    public Block[] readFriends() throws IOException, ClassNotFoundException {
        while (socketToHost == null) {                                // wait until socket ready
            try {
                sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        ooOut.writeObject(new Command(COMMAND_GET_FRIENDS, null, null, null));


  //      ObjectInputStream objectInputStream = new ObjectInputStream(socketToHost.getInputStream());

  //      String retString = (String) objectInputStream.readObject();  ///                             read OBJECT here

        return new Block[1];
    }

    public static void main(String[] args) {

        try {
            GameMap map = new GameMap();
            MapServer server = new MapServer(map, 8888);
            server.startAccept();

            Socket socketToHost = new Socket("localhost", 8888);

            ObjectOutputStream ooOut = new ObjectOutputStream(socketToHost.getOutputStream());
            ooOut.writeObject(MapServer.COMMAND_REG_CAR + ", 100, 111, 222");

            System.out.println("Write success");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
