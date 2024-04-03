import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class Block {
    static final int TYPE_BACKGROUND=0;
    static final int TYPE_WALL=1;
    static final int TYPE_PLAYER=9;
    BufferedImage image;
    int x, y;
    int type = TYPE_BACKGROUND;

    public Block() {
    }

    public void setLocation(int x, int y){
        this.x =x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public void setBlockImage(String filePath) {
        if(this.image == null){
            image = new BufferedImage(GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);;
        }

        Graphics g = image.getGraphics();
        g.setColor(Color.gray);
        g.fillRect(0,0 , GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE);

    }

    public BufferedImage getImage() {
        return image;
    }
}
