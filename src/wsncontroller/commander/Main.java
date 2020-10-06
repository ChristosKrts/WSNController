package wsncontroller.commander;

import wsncontroller.MatrixUtil.Coordinates;
import wsncontroller.activeMQ.ConsumerData;
import wsncontroller.dataBaseUtil.Setup;

public class Main {

    static Thread CommanderThread ,ConsumerMSGThread , ConsumerDataThread;


    public static void main(String[] args) {

        Setup.Connect();
        Coordinates.getHandler();
        CommanderThread = new Thread(new Commander());
        CommanderThread.start();

        ConsumerMSGThread = new Thread(new ConsumerMSG());
        ConsumerMSGThread.start();

        // Queue that's listening for data from the nodes (Logger part)
        ConsumerDataThread = new Thread(new ConsumerData());
        ConsumerDataThread. start();

        try {
            CommanderThread.join();
            ConsumerMSGThread.join();
            ConsumerDataThread.join();
        } catch (InterruptedException e) {
            System.out.println("Unable to join threads");
        }
    }
}
