import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

class CommandProcessor extends Thread {
    public static int PS_ID;
    private Socket clientSocket;
    MapServer mapServer;

    Car car;

    int pid;

    boolean isActive = true;

    public CommandProcessor(Socket clientSocket, MapServer mapServer) {
        this.clientSocket = clientSocket;
        this.mapServer = mapServer;

        pid = PS_ID;
        PS_ID++;
    }

    @Override
    public void run() {                                     //  run of CommandProcessor
        try {
            ObjectOutputStream ooOut;           ObjectInputStream ooIn = new ObjectInputStream(clientSocket.getInputStream());
            Command cc = (Command) ooIn.readObject();
System.out.println(cc.command);
            while (cc.command != null) {
                switch (cc.command) {
                    case MapServer.COMMAND_REQ_MAP:
//                        ooOut =  new ObjectOutputStream(clientSocket.getOutputStream());
//                        int xxx[][] = mapServer.getGameMapObject().getMapData();
//                        ooOut.writeObject(xxx);
                        System.out.println("request map success");
                      break;

                    case MapServer.COMMAND_REG_CAR:
                        car = new Car();
                        Random r = new Random();
                        int randomCol = r.nextInt(mapServer.getGameMapObject().getColumnSize());
                        int randomRow = r.nextInt(mapServer.getGameMapObject().getRowSize());

                        while (!mapServer.getGameMapObject().isBackgroundType(randomRow, randomCol)) {
                            randomCol = r.nextInt(mapServer.getGameMapObject().getColumnSize());
                            randomRow = r.nextInt(mapServer.getGameMapObject().getRowSize());

                        }
                        car.setLocation(randomRow, randomCol);

                        mapServer.carList.add(this);
                        mapServer.getGameMapObject().getMapData()[1][r.nextInt(10)] = 1;
                        Command retCommand = new Command("register success.",  car, mapServer.getGameMapObject().getMapData(), null);
                        ooOut = new ObjectOutputStream(clientSocket.getOutputStream());
                        ooOut.writeObject(retCommand);
                        break;

                    case MapServer.COMMAND_MOVE_TO:
                        long id =(long) cc.p1;
                        int row = (int) cc.p2;
                        int col = (int) cc.p3;
System.out.println("commandProcessor car move to ID:" + id + " row:" + row + " col:" + col);
                        break;

                    case MapServer.COMMAND_GET_FRIENDS:
                        System.out.println("HELLO GET FRIEND");
                        System.out.println(this.extractCarList());
                      //  ooOut.writeObject(this.extractCarList());
                        break;
                    default:
                        System.out.println(">>>> MAP SERVER: UNKNOWN COMMAND, " + cc.command);

                }

                cc =(Command) ooIn.readObject();
            }
        } catch (IOException e) {
            System.out.println(">>>> MAPSERVER: HANDLE CLIENT COMMAND FAIL:" + pid);
            e.printStackTrace();
            System.out.println(" fail fail fail");
        } catch (ClassNotFoundException e) {
            System.out.println(">>>> MAPSERVER: HANDLE CLIENT COMMAND FAIL:"  + pid);
            e.printStackTrace();
        }

        isActive = false;
    }

    private String extractCarList() {
        String ret="";
        Iterator<CommandProcessor> i = mapServer.carList.iterator();

        while(i.hasNext()) {
            CommandProcessor e = i.next();
            if (e.isActive) {
                ret += e.car.getId() + ",";
                ret += e.car.getRow() + ",";
                ret += e.car.getColumn() + ";";
            }
        }

        return ret;
    }

}
