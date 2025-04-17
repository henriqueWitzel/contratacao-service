package br.com.challenge.contratacao.application.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public AmazonSQS amazonSQS() {
        return AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:4566", // URL do LocalStack
                        "us-east-1"              // Região fake (usada com LocalStack)
                ))
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials("test", "test") // LocalStack aceita qualquer valor
                ))
                .build();
    }
}
