import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;

public class GameMap {
    int row;
    BufferedImage backgroundImage;
    int column;
    String fileName = "map.txt";
    int mapData[][];

    BlockBackground blockBackground = new BlockBackground();
    BlockWall blockWall = new BlockWall();


    //////////////////////////////////////////////////////////////////
    public GameMap() {
        try {
            initMapFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
                System.out.println(allLine[j]);
            }

            mapData = new int[row][column];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    mapData[i][j] = Integer.parseInt(allLine[i].substring(j, j + 1));
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("GAME INIT ERROR: Invalid map file format.");
        }

    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int[][] getMapData() {
        return mapData;
    }

    public BufferedImage getBackgroundImage() {
        int w = column * GameMapComponent.TILE_SIZE;
        int h = row * GameMapComponent.TILE_SIZE;

        if (this.backgroundImage == null) {
            backgroundImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics g = backgroundImage.getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, w, h);

            ///    firsttime background image creat
            BufferedImage bbb = null;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    switch (mapData[i][j]) {
                        case Block.TYPE_WALL:
                            bbb = blockWall.getImage();
                            break;
                        case Block.TYPE_PLAYER:
                            break;
                        default:
                            bbb = blockBackground.getImage();

                    }
                    System.out.print(mapData[i][j]);
                    g.drawImage(bbb, j * GameMapComponent.TILE_SIZE, i * GameMapComponent.TILE_SIZE, null);
                }
                System.out.println();
            }
        }

        return backgroundImage;
    }

    public static void main(String[] args) {
        GameMap m = new GameMap();
    }
}
