package Throughput;

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

        // Variables for throughput calculation
        long startTime = System.currentTimeMillis();
        int messagesReceived = 0;
        int durationSeconds = 10; // Adjust the duration for measuring throughput

        // Measure throughput
        while (System.currentTimeMillis() - startTime < durationSeconds * 1000) {
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                // Process text message
                messagesReceived++;
            }
        }

        // Calculate throughput
        double throughput = (double) messagesReceived / durationSeconds;
        System.out.println("Throughput for Consume API Call: " + throughput + " messages/second");

        // Clean up
        consumer.close();
        session.close();
        connection.close();
    }
}
