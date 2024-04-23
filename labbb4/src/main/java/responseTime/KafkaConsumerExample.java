package responseTime;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class KafkaConsumerExample {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "messages";
        final long numOfMessages = 10000;
        // Kafka consumer configuration
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "file-consumer-group");
        consumerProps.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        consumerProps.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        consumerProps.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        // Consume messages
        KafkaConsumer<String, byte[]> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Arrays.asList(topic));
        long start, responseTime = 0;
        for (long i = 0; i < numOfMessages;) {
            start = System.nanoTime();
            ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofNanos(100));
//            for(ConsumerRecord<String,byte[]>record:records){
//                System.out.println("key: " + record.key() + ", partition: " + record.partition() + ", offset: " + record.offset()+ " value " + record.value());
//            }
//            if(records.count()>0)
//               System.out.println(records.count());
             i += records.count();
            responseTime += System.nanoTime() - start;

        }
        System.out.println("Avg Consumer response Time = " + (((double)responseTime)/numOfMessages)/1e6 + " ms");

    }
}
