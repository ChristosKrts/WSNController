package wsncontroller.activeMQ;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MQHandler {
    private ConsumerMQHandler consumerGrids;
    private ConsumerMQHandler consumerReplies;
    private ProducerMQHandler producerCommands;
    private ConsumerMQHandler consumerGridF;
    private ConsumerMQHandler consumerData;
    private String ACTIVEMQ_Username;
    private String ACTIVEMQ_Password;
    private String ACTIVEMQ_IP;
    private String QUEUE_SEND_GRID;
    private String QUEUE_SEND_GRID_FUSION;
    private String QUEUE_RECEIVE_COMMANDS_REPLY;
    private String QUEUE_RECEIVE_COMMANDS;
    private String QUEUE_RECEIVE_DATA;

     private static MQHandler mqhandler;

     public static  synchronized MQHandler getHandler(){
            if(mqhandler == null)
                mqhandler = new  MQHandler();
            return mqhandler;
     }

    public MQHandler() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("configuration.properties"));
            ACTIVEMQ_Username = prop.getProperty("username");
            ACTIVEMQ_Password = prop.getProperty("password");
            ACTIVEMQ_IP = prop.getProperty("activeMQ_IP");
            QUEUE_SEND_GRID = prop.getProperty("queue_send_grid");
            QUEUE_RECEIVE_COMMANDS_REPLY = prop.getProperty("queue_receive_commands_reply");
            QUEUE_RECEIVE_COMMANDS = prop.getProperty("queue_receive_commands");
            QUEUE_SEND_GRID_FUSION = prop.getProperty("queue_send_gridf");
            QUEUE_RECEIVE_DATA = prop.getProperty("queue_receive_data");

            consumerGrids = new ConsumerMQHandler(ACTIVEMQ_IP, QUEUE_SEND_GRID, ACTIVEMQ_Username, ACTIVEMQ_Password);
            consumerReplies = new ConsumerMQHandler(ACTIVEMQ_IP, QUEUE_RECEIVE_COMMANDS_REPLY, ACTIVEMQ_Username, ACTIVEMQ_Password);
            producerCommands = new ProducerMQHandler(ACTIVEMQ_IP, QUEUE_RECEIVE_COMMANDS, ACTIVEMQ_Username, ACTIVEMQ_Password);
            consumerGridF = new ConsumerMQHandler(ACTIVEMQ_IP, QUEUE_SEND_GRID_FUSION, ACTIVEMQ_Username, ACTIVEMQ_Password);
            consumerData = new ConsumerMQHandler(ACTIVEMQ_IP, QUEUE_RECEIVE_DATA, ACTIVEMQ_Username, ACTIVEMQ_Password);

        } catch (IOException ex) {
            Logger.getLogger(MQHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getGrid(){
        return consumerGrids.ReceiveMessageMQ();
    }

     public String getGridFusion(){
        return consumerGridF.ReceiveMessageMQ();
    }

    public String getMSGReply(){
        return consumerReplies.ReceiveMessageMQ();
    }

    public void SendCommand(String Command)
    {
        producerCommands.sendCommand(Command);
    }

    public String getData(){
        return consumerData.ReceiveMessageMQ();
    }
}
