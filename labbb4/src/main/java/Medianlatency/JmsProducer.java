package Medianlatency;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;

public class JmsProducer {
    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;

        try {
            // Create ConnectionFactory
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

            // Create Connection
            connection = connectionFactory.createConnection();
            connection.start();

            // Create Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create Destination (Queue or Topic)
            Destination destination = session.createQueue("Lab4_medianLatency");

            // Create MessageProducer
            producer = session.createProducer(destination);

            // Read the content of the file
            String fileName = "C:\\Users\\abdel\\Desktop\\Connect-4\\Jms-Vs-Kafka\\labbb4\\src\\main\\resources\\jms.txt"; // Change this to your file path

            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            reader.close();

            // Create a text message with file content


            // Measure the response time
            // Send the message
            for (int i = 0; i < 10000; i++) {
                TextMessage message = session.createTextMessage(content.toString());
                message.setLongProperty("timestamp", System.nanoTime());
                producer.send(message);
            }

        } catch (IOException | jakarta.jms.JMSException e) {
            e.printStackTrace();
        } finally {
            // Clean up
            if (producer != null) {
                try {
                    producer.close();
                } catch (jakarta.jms.JMSException e) {
                    e.printStackTrace();
                }
            }
            if (session != null) {
                try {
                    session.close();
                } catch (jakarta.jms.JMSException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (jakarta.jms.JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
