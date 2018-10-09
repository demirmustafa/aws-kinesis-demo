package io.github.demirmustafa.kinesisproducer.runner;

import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.UserRecord;
import io.github.demirmustafa.core.model.UserEvent;
import io.github.demirmustafa.kinesisproducer.configuration.KinesisProducerConfigProps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ProducerStartupRunner implements CommandLineRunner {

    private static final String PARTITION_KEY = "samplePartitionKey";

    private final KinesisProducer kinesisProducer;
    private final KinesisProducerConfigProps producerConfigProps;

    public void run(String... args) throws Exception {


        int itemCountToBeProduced = 10;
        rerun:
        for (; ; ) {
            if (itemCountToBeProduced <= 0) {
                itemCountToBeProduced = 10;
                Thread.sleep(3000);
                continue rerun;
            }

            produce(Long.valueOf(new Random().nextInt(100)));
            itemCountToBeProduced--;
        }

    }

    private void produce(Long userId) {
        UserEvent event = initializeEvent(userId);
        UserRecord record = new UserRecord();
        record.setStreamName(producerConfigProps.getStreamName());
        record.setPartitionKey(String.valueOf(userId * new Random().nextInt(100)));
        //record.setPartitionKey(PARTITION_KEY);
        //record.setExplicitHashKey(String.valueOf(userId * new Random().nextInt(100))); //to override partition key
        record.setData(getDataAsByteBuffer(event));
        kinesisProducer.addUserRecord(record);
    }

    private UserEvent initializeEvent(Long userId) {
        return UserEvent.builder()
                .userId(userId)
                .eventType("click")
                .detail("user click event")
                .build();
    }

    @SneakyThrows
    private ByteBuffer getDataAsByteBuffer(UserEvent event) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(event);
        return ByteBuffer.wrap(out.toByteArray());
    }
}
