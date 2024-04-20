package Throughput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsProducer {
    public static void main(String[] args) throws IOException, jakarta.jms.JMSException {
        // Create ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

        // Create Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        // Create Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create Destination (Queue or Topic)
        Destination destination = session.createQueue("TestQueue");

        // Create MessageProducer
        MessageProducer producer = session.createProducer(destination);

        // Read the message from file
        String messageContent = readFromFile("C:\\Users\\abdel\\Desktop\\labbb4\\src\\main\\resources\\message (18).txt");

        // Create a text message
        TextMessage message = session.createTextMessage(messageContent);

        // Variables for throughput calculation
        long startTime = System.currentTimeMillis();
        int messagesSent = 0;
        int durationSeconds = 10; // Adjust the duration for measuring throughput

        // Measure throughput
        while (System.currentTimeMillis() - startTime < durationSeconds * 1000) {
            producer.send(message);
            messagesSent++;
        }

        // Calculate throughput
        double throughput = (double) messagesSent / durationSeconds;
        System.out.println("Throughput for Produce API Call: " + throughput + " messages/second");

        // Clean up
        producer.close();
        session.close();
        connection.close();
    }

    // Method to read message content from file
    private static String readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
}