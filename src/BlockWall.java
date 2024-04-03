import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockWall extends Block{

    public BlockWall() {
        type = TYPE_WALL;
        setBlockImage("asset/wall.png");
    }

    public void setBlockImage(String filePath) {
        if(this.image == null){
            image = new BufferedImage(GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);;
        }

        Graphics g = image.getGraphics();
        g.setColor(Color.green);
        g.fillRect(0,0 , GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE);

    }
}
