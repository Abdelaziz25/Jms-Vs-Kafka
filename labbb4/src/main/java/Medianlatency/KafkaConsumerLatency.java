package Medianlatency;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.header.Header;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class KafkaConsumerLatency{

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "messages-latency";
        final long numOfMessages = 10000;

        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "file-consumer-group");
        consumerProps.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        consumerProps.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        consumerProps.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(topic));

        long responseTime = 0;
        List<Long> latencies = new ArrayList<>();

        while (latencies.size() < numOfMessages) {
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, byte[]> record : records) {
                long messageTimestamp = ByteBuffer.wrap(record.headers().lastHeader("timestamp").value()).getLong();
                latencies.add(System.nanoTime() - messageTimestamp);
            }
        }

        consumer.close();

        Collections.sort(latencies);
        double medianLatency;
        if (latencies.size() % 2 == 0) {
            medianLatency = (latencies.get(latencies.size() / 2 - 1) + latencies.get(latencies.size() / 2)) / 2.0;
        } else {
            medianLatency = latencies.get(latencies.size() / 2);
        }

        System.out.println("Median Latency For 10k Msg:  " + medianLatency / 1e6 + " ms");
    }
}


