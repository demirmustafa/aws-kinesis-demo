package io.github.demirmustafa.kinesisconsumer.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.amazonaws.services.kinesis.metrics.impl.NullMetricsFactory;
import io.github.demirmustafa.kinesisconsumer.core.RecordProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.util.UUID;

@Configuration
@EnableConfigurationProperties(KinesisConsumerConfigProps.class)
@RequiredArgsConstructor
public class KinesisConsumerConfig {

    private final KinesisConsumerConfigProps props;
    private final RecordProcessorFactory recordProcessorFactory;

    @Bean
    public Worker worker(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(props.getAwsAccessKey(), props.getAwsSecretKey());
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(props.getAppName(), props.getKinesisStreamName(), credentialsProvider, workerId())
                .withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON)
                .withRegionName(props.getAwsRegion())
                .withIdleTimeBetweenReadsInMillis(5000)
                .withKinesisEndpoint(props.getKinesisEndpoint())
                .withDynamoDBEndpoint(props.getDynamoDBEndpoint());

        final Worker worker = new Worker.Builder()
                .recordProcessorFactory(recordProcessorFactory)
                .config(config)
                .metricsFactory(new NullMetricsFactory())
                .build();
        return worker;
    }

    @SneakyThrows
    private String workerId(){
        return InetAddress.getLocalHost().getCanonicalHostName() + ":" + UUID.randomUUID();
    }

}
