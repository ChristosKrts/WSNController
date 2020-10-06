package wsncontroller.activeMQ;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumerMQHandler {

    private boolean transacted;
    private String url;
    private String queue;
    private String userName;
    private String password;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;

    public ConsumerMQHandler(String url, String queueName, String userName, String password) {
        this.url = url;
        this.queue = queueName;
        this.userName = userName;
        this.password = password;

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(this.url);
        try {
            this.connection = connectionFactory.createConnection(userName, password);
            // Create a session
            this.session = this.connection.createSession(this.transacted, Session.AUTO_ACKNOWLEDGE);

            this.destination = this.session.createQueue(this.queue);

           this.consumer = this.session.createConsumer(this.destination);

          // this.connection.setExceptionListener((ExceptionListener)this);

           this.connection.start();
           
        } catch (JMSException ex) {
            Logger.getLogger(ConsumerMQHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String ReceiveMessageMQ() {
        Message message = null;
        try {
            message = consumer.receive();
        } catch (JMSException ex) {
            Logger.getLogger(ConsumerMQHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String text = "";
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                text = textMessage.getText();
                //MatrixGrid matrix = new MatrixGrid(text);
            } catch (JMSException ex) {
                Logger.getLogger(ConsumerMQHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Recieved Error Message: "+message);
        }
        return text;
    }

     public synchronized void onException(JMSException exception) {
        System.err.println("Something bad happened: " + exception);
    }
}
