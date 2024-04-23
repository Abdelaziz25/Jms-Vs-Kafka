package responseTime;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer {
    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;

        long startTime = 0;
        long endTime = 0;

        try {
            // Create ConnectionFactory
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create Connection
            connection = connectionFactory.createConnection();
            connection.start();

            // Create Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create Destination (Queue or Topic)
            Destination destination = session.createQueue("Lab4");

            // Create MessageConsumer
            consumer = session.createConsumer(destination);

            // Start receiving messages
            startTime = System.currentTimeMillis(); // Start time in milliseconds
            while (true) {
                // Receive message
                Message message = consumer.receive();

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    // Print message content
                    System.out.println("Received message: " + textMessage.getText());
                    break; // Exit the loop when message is received
                } else {
                    System.out.println("Received non-text message: " + message);
                }
            }
            endTime = System.currentTimeMillis();
            double responseTimeMilliseconds = (endTime - startTime); // Response time in milliseconds
            System.out.println("Response Time for Consumer: " + responseTimeMilliseconds + " milliseconds");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            // Clean up
            if (consumer != null) {
                try {
                    consumer.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
