package wsncontroller.activeMQ;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ProducerMQHandler {

    private boolean transacted;
    private String url;
    private String queue;
    private String userName;
    private String password;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    public ProducerMQHandler(String url, String queueName, String userName, String password) {
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

            this.producer = this.session.createProducer(this.destination);

            // this.connection.setExceptionListener((ExceptionListener)this);

            this.connection.start();

        } catch (JMSException ex) {
            Logger.getLogger(ConsumerMQHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendCommand(String cmd)
    {
        TextMessage message;
        try {
            message = this.session.createTextMessage(cmd);
            this.producer.send(message);
        } catch (JMSException ex) {
            Logger.getLogger(ProducerMQHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
