
import java.io.*;
import java.util.StringTokenizer;

public class Map {
    int width;



    int height;
    String fileName = "map.txt";
    int map[][];


    //////////////////////////////////////////////////////////////////
    public Map() {
        try {
            initMap();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void initMap () throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        // first line contain map size
        String line = br.readLine();
        map = new int[10][10];

        try {

            StringTokenizer st = new StringTokenizer(line,"x");
            int w = Integer.parseInt(st.nextToken());
            int h  = Integer.parseInt(st.nextToken());
            this.width = w;
            this.height = h;

            String allLine[] = new String[h];
            String space = "0".repeat(w);

            for(int j=0; j<h; j++){
                line = br.readLine();
                if(line != null) {
                    String tmp = line + space;
                    allLine[j] = tmp.substring(0,w);
                } else {
                    allLine[j] = space;
                }

                allLine[j] = allLine[j].replace(" ", "0");
                System.out.println(allLine[j]);
            }

            map = new int[h][w];
            for(int i=0; i < h; i++){
                for(int j=0; j < w; j++) {
                    map[i][j] = Integer.parseInt(allLine[i].substring(j, j+1));
                    System.out.print("" + map[i][j]);
                }
                System.out.println("");
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("GAME INIT ERROR: Invalid map file format.");
        }

    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public static void main(String[] args) {
        Map m = new Map();
    }
}
