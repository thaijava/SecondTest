import java.io.IOException;

public class UpdateMeFriendAndItems extends Thread {
    MapServer mapServer;
    private Car car;

    public UpdateMeFriendAndItems(Car car, MapServer mapServer) {
        this.mapServer = mapServer;
        this.car = car;

    }

    public void run() {
        int oldRow = car.getRow();
        int oldCol = car.getColumn();

        while(true) {
            try {
                if(oldRow != car.getRow() || oldCol != car.getColumn()) {

                    mapServer.carMoveTo(car.getId(), car.getRow(), car.getColumn());

                    oldCol = car.getColumn();
                    oldRow = car.getRow();
                }

                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                mapServer.isOnline(false);
            }
        }
    }

    public void setCar(Car c) {
        this.car = c;
    }
}
