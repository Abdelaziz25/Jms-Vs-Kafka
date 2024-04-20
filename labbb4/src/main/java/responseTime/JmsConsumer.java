package responseTime;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsConsumer {
    public static void main(String[] args) throws JMSException {
        // Create ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Create Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create Destination (Queue or Topic)
        Destination destination = session.createQueue("TestQueue");

        // Create MessageConsumer
        MessageConsumer consumer = session.createConsumer(destination);

        // Start receiving messages
        long startTime = System.nanoTime(); // Record start time
        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    System.out.println("Received message: " + ((TextMessage) message).getText());
                    long endTime = System.nanoTime(); // Record end time
                    double responseTimeSeconds = (endTime - startTime) / 1e6; // Calculate response time in seconds
                    System.out.println("Response Time for Receive API Call: " + responseTimeSeconds + " seconds");
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        // Wait for messages
        try {
            Thread.sleep(10000); // Adjust time to wait for messages
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Clean up
        session.close();
        connection.close();
    }
}
