package com.br.spring_localstack.config;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

import java.net.URI;
import java.time.Duration;

@Configuration
public class CloudWatchMetricsConfig {

    @Value("${cloud.aws.endpoint.url}")
    private String endpointUrl;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public CloudWatchAsyncClient cloudWatchAsyncClient() {
        return CloudWatchAsyncClient.builder()
                .endpointOverride(URI.create(endpointUrl))
                .region(Region.of(region))
                .httpClient(NettyNioAsyncHttpClient.builder().build())
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")
                ))
                .build();
    }

    @Bean
    public CloudWatchConfig cloudWatchConfig() {
        return new CloudWatchConfig() {
            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public String namespace() {
                return "MyAppMetrics";
            }

            @Override
            public Duration step() {
                return Duration.ofMinutes(1);
            }
        };
    }

    @Bean
    public CloudWatchMeterRegistry meterRegistry(CloudWatchConfig cloudWatchConfig, CloudWatchAsyncClient cloudWatchAsyncClient) {
        CloudWatchMeterRegistry registry = new CloudWatchMeterRegistry(cloudWatchConfig, Clock.SYSTEM, cloudWatchAsyncClient);
        registry.config().meterFilter(MeterFilter.renameTag("aws.requests", "service", "aws_service"));
        return registry;
    }
}