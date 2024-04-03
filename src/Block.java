import java.awt.*;
import java.awt.image.BufferedImage;

public class Block {
    static final int TYPE_BACKGROUND=0;
    static final int TYPE_WALL=1;
    static final int TYPE_PLAYER=9;
    BufferedImage image;
    int row, column;
    int type = TYPE_BACKGROUND;

    public Block() {
    }

    public void setLocation(int x, int y){
        this.column =x;
        this.row = y;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getType() {
        return type;
    }

    public void setFile(String filePath) {
        if(this.image == null){
            image = new BufferedImage(GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
        }

        Graphics g = image.getGraphics();
        g.setColor(Color.gray);
        g.fillRect(0,0 , GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE);

    }

    public BufferedImage getImage() {
        return image;
    }
}
