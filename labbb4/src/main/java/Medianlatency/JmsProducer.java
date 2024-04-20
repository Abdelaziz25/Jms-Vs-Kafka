package Medianlatency;

import java.io.*;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JmsProducer {
    public static void main(String[] args) throws IOException, JMSException {
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

        // Measure the production time
        long startTime = System.nanoTime();
        message.setLongProperty("startTime", startTime); // Set production time as message property

        // Send the message
        producer.send(message);

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
