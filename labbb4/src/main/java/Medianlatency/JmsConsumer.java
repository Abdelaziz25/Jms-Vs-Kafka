package Medianlatency;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JmsConsumer {
    public static void main(String[] args) {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;

        List<Long> latencies = new ArrayList<>(); // List to store latencies

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

            // Create MessageConsumer
            consumer = session.createConsumer(destination);

            // Start receiving messages
            for (int i = 0; i < 10000; i++) { // Consume 10K messages
                // Receive message
                Message message = consumer.receive();
                long timestamp = message.getJMSTimestamp();
                latencies.add((System.nanoTime() - timestamp));
            }

            // Sort the list of latencies
            Collections.sort(latencies);

            // Calculate median latency
            double medianLatency;
            if (latencies.size() % 2 == 0) {
                medianLatency = (latencies.get(latencies.size() / 2 - 1) + latencies.get(latencies.size() / 2)) / 2.0;
            } else {
                medianLatency = latencies.get(latencies.size() / 2);
            }

            System.out.println("Median Latency for consuming 10K messages: " + medianLatency + " nanoseconds");
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            // Clean up
            try {
                if (consumer != null) consumer.close();
                if (session != null) session.close();
                if (connection != null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
