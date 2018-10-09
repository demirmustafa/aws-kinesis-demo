package io.github.demirmustafa.kinesisconsumer.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "consumer")
@Getter
@Setter
public class KinesisConsumerConfigProps {

    private String awsAccessKey;
    private String awsSecretKey;
    private String awsRegion;
    private String dynamoDBEndpoint;
    private String kinesisEndpoint;
    private String kinesisStreamName;
    private String platform;
    private String appName;


}
