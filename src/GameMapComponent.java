import javax.swing.*;
import java.awt.*;


public class GameMapComponent extends JComponent {
    public static int BASE_TILE_SIZE  = 16; // one block 16x16
    public static int  scale = 3;
    public static int TILE_SIZE = BASE_TILE_SIZE * scale;


    GameMap map = new GameMap();

    public GameMapComponent(){
        init();

    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.drawImage(map.getBackgroundImage(), 0,0, null);
    }


    // in init  Creat game background image

    private void init() {
        int w = map.getColumn() * TILE_SIZE ;
        int h = map.getRow() * TILE_SIZE ;

        this.setPreferredSize(new Dimension(w, h));
    }


    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());

        GameMapComponent g = new GameMapComponent();
        f.add(g, BorderLayout.CENTER);

        f.pack();
        f.setVisible(true);
    }
}