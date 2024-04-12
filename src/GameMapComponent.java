import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;


public class GameMapComponent extends JComponent implements Runnable, KeyListener, ComponentListener {
    int fps = 120;                                               //       FPS
    public static int BASE_TILE_SIZE = 16;                  // one block 16x16
    public static double scale = 1;                         //     SCALE
    public static int TILE_SIZE = (int) (BASE_TILE_SIZE * scale);
    boolean upPressed = false;
    boolean downPressed = false;
    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean switch01 = false;
    int TARGET_TIME = 1000000000 / fps;
    UpdateMeFriendAndItems updateMeFriendAndItems;
    Car car;
    BufferedImage carImage = null;
    Thread mainLoopThread;
    GameMap gameMap;
    MapServer mapServer;
    Dimension selfSize = new Dimension(400, 400);

    int regCount;

    public GameMapComponent( ) throws IOException, ClassNotFoundException{

        gameMap = new GameMap();

        init();  // graphic component size

        mapServer = new MapServer(gameMap, 8888);

        mapServer.startAccept();

        Command retComand = mapServer.registerCar();
        car = (Car) retComand.p1;

        System.out.println("after reg car " + car);

        updateMeFriendAndItems = new UpdateMeFriendAndItems(car, mapServer);

        updateMeFriendAndItems.start();

    }

    private void init() {

        mainLoopThread = new Thread(this);
        mainLoopThread.start();

        this.addKeyListener(this);

        this.addComponentListener(this);

    }


    private void readKeyDirection() {
        if (upPressed) {
            try {
                if (gameMap.isWallType(car.getRow() - 1, car.getColumn())) return;
            } catch (IndexOutOfBoundsException e) {
                car.row = mapServer.getGameMapObject().getRowSize();
                car.y = car.row * TILE_SIZE;
            }

            int tmp = car.row - 1;
            tmp *= TILE_SIZE;

            long lastTime;
            while (car.y > tmp) {
                car.y -= car.speed;
                lastTime = System.nanoTime();
                repaint();

                long timeConsume = System.nanoTime() - lastTime;

                if (timeConsume < TARGET_TIME) {
                    long sleepTime = (TARGET_TIME - timeConsume) / 1000000;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            car.xyToRowColumn();

        } else if (downPressed) {
            try {
                if (gameMap.isWallType(car.getRow() + 1, car.getColumn())) return;
            } catch (IndexOutOfBoundsException e) {
                car.row = 0;
                car.y = 0;
            }

            int tmp = car.getRow() + 1;
            tmp *= TILE_SIZE;

            long lastTime;
            while (car.y < tmp) {
                car.y += car.speed;
                lastTime = System.nanoTime();
                repaint();

                long timeConsume = System.nanoTime() - lastTime;
                if (timeConsume < TARGET_TIME) {
                    long sleepTime = (TARGET_TIME - timeConsume) / 1000000;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            car.xyToRowColumn();

        } else if (leftPressed) {
            try {
                if (gameMap.isWallType(car.getRow(), car.getColumn() - 1)) return;
            } catch (IndexOutOfBoundsException e) {
                car.column = mapServer.getGameMapObject().getColumnSize();
                car.x = car.column * TILE_SIZE;
            }

            int c = car.getColumn() - 1;
            c *= TILE_SIZE;
            long lastTime;
            while (car.x > c) {
                car.x -= car.speed;
                lastTime = System.nanoTime();
                repaint();

                long timeConsume = System.nanoTime() - lastTime;
                if (timeConsume < TARGET_TIME) {
                    long sleepTime = (TARGET_TIME - timeConsume) / 1000000;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            car.xyToRowColumn();

        } else if (rightPressed) {

            try {
                if (gameMap.isWallType(car.getRow(), car.getColumn() + 1)) return;
            } catch (IndexOutOfBoundsException e) {
                car.column = 0;
                car.x = 0;
            }

            int c = car.getColumn() + 1;
            c *= TILE_SIZE;
            long lastTime;
            while (car.x < c) {
                car.x += car.speed;
                lastTime = System.nanoTime();
                repaint();

                long timeConsume = System.nanoTime() - lastTime;
                if (timeConsume < TARGET_TIME) {
                    long sleepTime = (TARGET_TIME - timeConsume) / 1000000;
                    try {
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            car.xyToRowColumn();
        }


    }


    public void run() {

        long lastTime;

        while (mainLoopThread != null) {
            lastTime = System.nanoTime();

            readKeyDirection();

            repaint();

            long timeConsume = System.nanoTime() - lastTime;

            if (timeConsume < TARGET_TIME) {
                long sleepTime = (TARGET_TIME - timeConsume) / 1000000;
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

        g.drawImage(gameMap.getBackgroundImage(), 0, 0, selfSize.width, selfSize.height, null);

        if (carImage == null) carImage = car.getImage();

        double rescaleX = (TILE_SIZE * gameMap.getColumnSize()) / selfSize.getWidth();
        double rescaleY = (TILE_SIZE * gameMap.getRowSize()) / selfSize.getHeight();

        int carSizeX = (int) (GameMapComponent.TILE_SIZE / rescaleX);
        int carSizeY = (int) (GameMapComponent.TILE_SIZE / rescaleY);

        g.drawImage(carImage, (int) (car.x / rescaleX), (int) (car.y / rescaleY), carSizeX, carSizeY, null);

        switch01 = !switch01;
    }


    public static void main(String[] args) {
        JFrame f = new JFrame();
        JTextField remoteHostTextField = new JTextField("localhost");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());

        GameMapComponent gameMapComponent;
        try {
            gameMapComponent = new GameMapComponent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        f.add(gameMapComponent, BorderLayout.CENTER);
        f.add(remoteHostTextField, BorderLayout.NORTH);

        f.setSize(800, 800);
        f.setLocation(300, 0);
        f.setVisible(true);
        gameMapComponent.requestFocus();
    }


    @Override
    public void keyTyped(KeyEvent e) {
        try {
            if (e.getKeyChar() == 'x') {

                mapServer.connectRemote("192.168.50.55", 8888);

                Command retCommand = mapServer.registerCar();

                car = (Car) retCommand.p1;
                gameMap = new GameMap((int[][]) retCommand.p2);
System.out.println("after connect remote " + car);
                updateMeFriendAndItems.setCar(car);

            } else if (e.getKeyChar() == 'y') {
                mapServer.readFriends();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        int w = e.getComponent().getWidth();
        int h = e.getComponent().getHeight();

        if (e.getComponent().getWidth() < 400)
            w = 400;
        if (e.getComponent().getHeight() < 400)
            h = 400;

        selfSize.setSize(w, h);

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}