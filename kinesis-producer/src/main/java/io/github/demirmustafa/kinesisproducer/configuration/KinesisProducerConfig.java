package io.github.demirmustafa.kinesisproducer.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.kinesis.producer.KinesisProducer;
import com.amazonaws.services.kinesis.producer.KinesisProducerConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KinesisProducerConfigProps.class)
@RequiredArgsConstructor
public class KinesisProducerConfig {

    private final KinesisProducerConfigProps props;

    @Bean
    public KinesisProducer kinesisProducer(){
        BasicAWSCredentials credentials = new BasicAWSCredentials(props.getAwsAccessKey(), props.getAwsSecretKey());
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        KinesisProducerConfiguration producerConfiguration = new KinesisProducerConfiguration()
                .setRegion(props.getAwsRegion())
                .setVerifyCertificate(props.getUseSsl())
                .setCredentialsProvider(credentialsProvider)
                .setRecordTtl(8 * 1000 * 60) //kinesise datayı yazamaz ise, ne kadar süre boyunca datayı localde bufferlasın
                .setMaxConnections(10)
                //.setRecordMaxBufferedTime(100000) //datayı ne kadar bufferlaryıpta kinesise atsın, default 100ms
                .setKinesisEndpoint(props.getEndpoint())
                .setKinesisPort(props.getPort());

        return new KinesisProducer(producerConfiguration);
    }
}
