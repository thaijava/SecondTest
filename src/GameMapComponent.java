import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class GameMapComponent extends JPanel implements Runnable{
    public static int BASE_TILE_SIZE  = 16; // one block 16x16
    public static double  scale = 2.8;                          //     SCALE
    public static int TILE_SIZE = (int)(BASE_TILE_SIZE * scale);

    int fps = 65;                                               //       FPS
    int TARGET_TIME = 1000000000/fps;

    Car player = null;
    BufferedImage playerImage=null;

    Thread componentThread;

    GameMap mapObject = new GameMap();

    KeyState keyState = new KeyState();
    public GameMapComponent(){
        init();  // graphic component size

    }


    private void startAllThreads() {
        componentThread = new Thread(this);
        componentThread.start();
    }

    private void readKey() {
        if (keyState.upPressed) {
            player.moveUp();
        } else if (keyState.downPressed)
            player.moveDown();
        else if (keyState.leftPressed)
            player.moveLeft();
        else if (keyState.rightPressed)
            player.moveRight();

    }


    public void run() {

        long currentTime ;

        while(componentThread != null) {
            currentTime = System.nanoTime();
            readKey();
            repaint();

            long timeConsume = System.nanoTime()- currentTime;
            if(timeConsume < TARGET_TIME){
                long sleepTime = (TARGET_TIME - timeConsume)/ 1000000;

                try {
                    Thread.sleep(sleepTime);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    ///////////////////////////////   PAINT COMPONENT
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        g.drawImage(mapObject.getBackgroundImage(), 0,0, null);
        if (playerImage == null) playerImage = player.getImage();

        g.drawImage(playerImage, player.getX(), player.getY(), GameMapComponent.TILE_SIZE, GameMapComponent.TILE_SIZE, null);


    }


    // in init  Creat game background image

    private void init() {
        int w = mapObject.getColumn() * TILE_SIZE ;
        int h = mapObject.getRow() * TILE_SIZE ;

        this.setPreferredSize(new Dimension(w, h));

        player = new Car(mapObject);

        player = mapObject.registerCar(player);

        startAllThreads();

        this.addKeyListener(keyState);

        this.setFocusCycleRoot(true);
    }


    public void setScale(double newScale) {
        scale = newScale;
        TILE_SIZE = (int)(BASE_TILE_SIZE * scale);

    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());

        GameMapComponent gameMapComponent = new GameMapComponent();
        f.add(gameMapComponent, BorderLayout.CENTER);

        f.pack();
        f.setLocation(300, 0);
        f.setVisible(true);
        gameMapComponent.requestFocus();
    }


}