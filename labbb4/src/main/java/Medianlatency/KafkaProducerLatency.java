package Medianlatency;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.header.Header;

import java.nio.ByteBuffer;
import java.util.Properties;

public class KafkaProducerLatency {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "messages-latency";
        final long numOfMessages = 10000;

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(producerProps);
        String key = "my-key";
        byte[] value = new byte[1024];

        long startTime, responseTime = 0;
        for (long i = 0; i < numOfMessages; i++) {
            startTime = System.nanoTime();
            byte[] timestampBytes = ByteBuffer.allocate(Long.BYTES).putLong(startTime).array();
            ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, key, value);
            record.headers().add("timestamp", timestampBytes);
            producer.send(record);
            responseTime += System.nanoTime() - startTime;
        }

        double produceTime = ((double) responseTime / numOfMessages / 1e6);
        System.out.println("Avg Produce response time: " + produceTime + " ms");
        producer.flush();
        producer.close();
    }
}
