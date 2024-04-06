public class Car extends Block {

    GameMap gameMap;
    int speed = GameMapComponent.TILE_SIZE / 11;           // SPEED

    int x, y;

    int headAngle=0;

    public Car(GameMap gameMap) {
        this.gameMap = gameMap;

        type = Block.TYPE_CAR;
        this.setFileImage("asset/redcar.png");
    }

    public String toString() {
        return "col:" + column + " row:" + row;
    }

    @Override
    public void setLocation(int row, int column) {
        super.setLocation(row, column);

        x = column * GameMapComponent.TILE_SIZE;
        y = row * GameMapComponent.TILE_SIZE;
    }

    private void updateLocation() {
        int refX = column * GameMapComponent.TILE_SIZE;
        int refY = row * GameMapComponent.TILE_SIZE;
        int half = GameMapComponent.TILE_SIZE / 3;

        int boundRight = refX + half;
        int boundLeft = refX - half;

        int boundUpper = refY - half;
        int boundLower = refY + half;

        if (x > boundRight) {
            column += 1;
        } else if (x < boundLeft) {
            column -= 1;
        } else if (y < boundUpper) {
            row -= 1;
        } else if (y > boundLower) {
            row += 1;
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeadAngle() { return headAngle; }


    public void moveUp() {
        headAngle = 0;

        int mapData[][] = gameMap.getMapData();
        try {
            if (mapData[row - 1][column] == Block.TYPE_WALL && y < row * GameMapComponent.TILE_SIZE)
                return;
        } catch (Exception e) {
            return;
        }

        y-= speed;
        updateLocation();

    }

    public void moveDown() {
        headAngle = 180;

        int mapData[][] = gameMap.getMapData();
        try {
            if (mapData[row + 1][column] == Block.TYPE_WALL && y > row * GameMapComponent.TILE_SIZE)
                return;
        } catch (Exception e) {
            return;
        }

        y += speed;
        updateLocation();
    }

    public void moveLeft() {
        headAngle = 270;

        int mapData[][] = gameMap.getMapData();
        try {
            if (mapData[row ][column -1] == Block.TYPE_WALL && x < column * GameMapComponent.TILE_SIZE)
                return;
        } catch (Exception e) {
            if(column == 0) {
                column=gameMap.getColumn();
                x = column * GameMapComponent.TILE_SIZE;
            }
        }

        x -= speed;
        updateLocation();
    }

    public void moveRight() {
        headAngle = 90;

        int mapData[][] = gameMap.getMapData();
        try {
            if (mapData[row ][column +1] == Block.TYPE_WALL && x > column * GameMapComponent.TILE_SIZE)
                return;
        } catch (Exception e) {
            if(column == gameMap.getColumn()) {
                x=0;
                column=0;
            }

        }

        x += speed;
        updateLocation();
    }

}
