import java.awt.*;
import java.awt.image.BufferedImage;

public class BlockPlayer extends  Block{

    public BlockPlayer() {
        type = Block.TYPE_PLAYER;
        setFile("asset/player0.png");
    }

    public void setFile(String filePath) {
        if(this.image == null){
            image = new BufferedImage(GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);;
        }

        Graphics g = image.getGraphics();
        g.setColor(Color.red);
        g.drawLine(0,0 , GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE);
        g.drawLine(0,GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE, 0);

    }

}
