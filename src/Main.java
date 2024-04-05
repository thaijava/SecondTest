import java.io.*;
import java.net.*;



public class Main {
    public static void main(String[] args) {

            try (Socket socket = new Socket("192.168.50.55", 8080);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
                out.println("REQ MAP DATA");

                ObjectInputStream objectInputStream
                        = new ObjectInputStream(socket.getInputStream());
                int[][] mapData = (int[][])objectInputStream.readObject();  ////////////// read oooobject here
                objectInputStream.close();

                GameMap map = new GameMap(mapData);
                map.getBackgroundImage();
            } catch (UnknownHostException e) {
                System.err.println("MAIN.JAVA: localhost");
            } catch (IOException e) {
                System.err.println("MAIN.JAVA: I/O error occurred when creating the socket or sending the command");
            } catch (ClassNotFoundException e) {
                System.err.println("MAIN.JAVA: OBJECT CLASS NOT FOUND");
            }
    }
}