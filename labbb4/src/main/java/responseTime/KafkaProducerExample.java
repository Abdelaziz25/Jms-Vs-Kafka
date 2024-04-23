package responseTime;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class KafkaProducerExample {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "messages";
        final long numOfMessages = 10000;

        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());


//        String filePath = "src/main/resources/message (18).txt";
        KafkaProducer<String, byte[]> producer = new KafkaProducer<>(producerProps);
        String key = "my-key";
        byte[] value = new byte[1024];

        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, key, value);
        // Produce messages
        long startTime , responseTime = 0; // Record start time
       for(long i=0 ; i <numOfMessages;i++) {
           startTime =System.nanoTime();
           producer.send(record);
           responseTime+= System.nanoTime() - startTime;

       }

        double produceTime = ((double) responseTime /numOfMessages/1e6); // Calculate response time
        System.out.println(" Avg Produce response time: " + produceTime + " ms");
        producer.flush();
        producer.close();
    }
}

