package Medianlatency;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JmsConsumer {
    public static void main(String[] args) throws JMSException {
        List<Long> latencies = new ArrayList<>(); // List to store latencies

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
        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    long endTime = System.nanoTime(); // Record consumption time
                    long startTime = ((TextMessage) message).getLongProperty("startTime"); // Retrieve production time
                    long latency = endTime - startTime;
                    latencies.add(latency); // Store latency

                    System.out.println("Received message: " + ((TextMessage) message).getText());
                    System.out.println("Latency: " + latency + " nanoseconds");
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
        consumer.close();
        session.close();
        connection.close();

        // Calculate and print median latency
        double medianLatency = calculateMedian(latencies);
        System.out.println("Median Latency: " + medianLatency + " nanoseconds");
    }

    private static double calculateMedian(List<Long> latencies) {
        // Sort latencies
        Collections.sort(latencies);

        int size = latencies.size();
        // Calculate median
        if (size % 2 == 0) {
            return (latencies.get(size / 2 - 1) + latencies.get(size / 2)) / 2.0;
        } else {
            return latencies.get(size / 2);
        }
    }
}
