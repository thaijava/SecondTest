import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;

public class GameMap implements Serializable{
    int row;
    BufferedImage backgroundImage;
    int column;
    String fileName = "map.txt";

    int mapData[][];

    Car car;


    BlockBackground blockBackground = new BlockBackground();
    BlockWall blockWall = new BlockWall();


    //////////////////////////////////////////////////////////////////
    public GameMap() throws IOException {

            initMapFromFile();


    }

    public GameMap(int[][] mapData) {
        row = mapData.length;
        column = mapData[0].length;

        this.mapData = mapData;
        backgroundImage = null;
    }


    private void initMapFromFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        // first line contain map size
        String line = br.readLine();
        mapData = new int[10][10];

        try {

            StringTokenizer st = new StringTokenizer(line, "x");
            int w = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            this.column = w;
            this.row = h;

            String allLine[] = new String[h];
            String space = "0".repeat(w);

            for (int j = 0; j < row; j++) {
                line = br.readLine();
                if (line != null) {
                    String tmp = line + space;
                    allLine[j] = tmp.substring(0, column);
                } else {
                    allLine[j] = space;
                }

                allLine[j] = allLine[j].replace(" ", "0");

            }

            mapData = new int[row][column];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    mapData[i][j] = Integer.parseInt(allLine[i].substring(j, j + 1));
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(">>>> GAMEMAP INIT mapfile ERROR: Invalid map file format.");
        }

    }

    public int getRowSize() {
        return row;
    }

    public int getColumnSize() {
        return column;
    }

    public  int[][] getMapData() {
        return mapData;
    }

    public boolean isWallType(int r, int c) throws IndexOutOfBoundsException {
        if(mapData[r][c] == Block.TYPE_WALL) return true;
        return false;
    }
     public boolean isBackgroundType(int r, int c) throws IndexOutOfBoundsException {
            if(mapData[r][c] == Block.TYPE_BACKGROUND) return true;
            return false;
    }

    public BufferedImage getBackgroundImage() {
        int w = column * GameMapComponent.TILE_SIZE;
        int h = row * GameMapComponent.TILE_SIZE;

        if (this.backgroundImage == null) {
            System.out.println(">>>> GameMap.java REBUILD MAP.");
            backgroundImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = backgroundImage.getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, w, h);
            BufferedImage blockBackgroundImage = null;
            BufferedImage wallImage = null;

            ///    firsttime background image creat
            BufferedImage bbb = null;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    switch (mapData[i][j]) {
                        case Block.TYPE_WALL:
                            if(blockBackgroundImage == null)
                                blockBackgroundImage = blockWall.getImage();
                            bbb = blockBackgroundImage;
                            break;

                        default:
                            if(wallImage == null)
                                wallImage = blockBackground.getImage();

                            bbb = wallImage;

                    }

                    g.drawImage(bbb, j * GameMapComponent.TILE_SIZE, i * GameMapComponent.TILE_SIZE,
                            GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE, null);
                }

            }
        }

        return backgroundImage;
    }



    public static void main(String[] args) {
        try {
            GameMap map = new GameMap();
            MapServer server = new MapServer(map, 8888);
            server.startAccept();

            Car c = new Car();

            System.out.println(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
