package wsncontroller.commander;

import java.util.Random;
import wsncontroller.dataBaseUtil.Queries;
import wsncontroller.MatrixUtil.Coordinates;
import wsncontroller.node.Node;
import java.util.StringTokenizer;
import wsncontroller.MatrixUtil.Coord;
import wsncontroller.activeMQ.MQHandler;

public class ConsumerMSG  extends Thread{

    public void msgReceirer()
    {
        System.out.println("Listening for Reply msgs... ");
        while(true){
            String str1 = MQHandler.getHandler().getMSGReply();
            System.out.println("Received from proxy: " + str1);
            parseMSG(str1);
        }
    }

    @Override
    public void run()
    {
        msgReceirer();
    }

    private void parseMSG(String str1) {
        StringTokenizer st = new StringTokenizer(str1, " ");
        String NodeID = st.nextToken();
        String msgType = st.nextToken();
        

        if(msgType.equals("EnergyMode")){
            String value = st.nextToken();
            wsncontroller.dataBaseUtil.Queries.UpdateState(NodeID, value);
        }
        else if(msgType.equals("Frequency")){
            String value = st.nextToken();
            try{
                wsncontroller.dataBaseUtil.Queries.UpdateFreq(NodeID, Integer.parseInt(value));
            }
            catch (NumberFormatException e) {
                System.out.println("java.lang.NumberFormatException: For input string: " + value+ " .Doing nothing...");
            }
        }
        else if (msgType.equals("Insert")){
            String value = st.nextToken();
            String value2  = st.nextToken();
             if(Queries.checkIfExists(NodeID) == false) {
                 Coord coord =Coordinates.getHandler().getCoordByID(NodeID);
                 if (coord== null){
                     System.out.println("Error at getting coordinates for the node with ID: " + NodeID);
                 }
                 else{
                    Queries.InsertNode(NodeID,Integer.parseInt(value), value2, coord.getX(),coord.getY(),coord.getQuadrant()); //insert node to database
                    Random randomGenerator = new Random();
                    // Generate a random initial state for the node///
                    int randomInt = randomGenerator.nextInt(2);
                    if (randomInt == 1) {
                        String message = NodeID + " Energy  " + "Wake";
                        MQHandler.getHandler().SendCommand(message); //Send message to node
                    }

                     // Generate a random initial frequency for the node///
                    int randomInt2 = randomGenerator.nextInt(11);
                     String message2 = NodeID + " Sampling  " + randomInt2*1000;
                     MQHandler.getHandler().SendCommand(message2); //Send message to node
                 }
            }
            else  //Initialize the node, if it already exists in database
            {
                System.out.println(" Node Already exists in database... Initialize its things!!---------------");
                Queries.UpdateFreq(NodeID, Integer.parseInt(value));
                Queries.UpdateState(NodeID, value2);
            }

        }
        else if (msgType.equals("Delete")) {
            Queries.deleteNodeRecord(NodeID);
        }
        else{
            System.out.println("Unknown Command " + msgType);
        }

    }

}
