package io.github.demirmustafa.kinesisproducer.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "kinesis")
public class KinesisProducerConfigProps {

    private String endpoint;
    private Integer port;
    private String awsAccessKey;
    private String awsSecretKey;
    private String awsRegion;
    private String streamName;
    private Boolean useSsl;
}
