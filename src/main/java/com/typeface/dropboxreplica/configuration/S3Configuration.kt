package com.typeface.dropboxreplica.configuration

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class S3Configuration(private val awsConfigurationProperties: AwsConfigurationProperties) {

    @Bean(destroyMethod = "shutdown")
    open fun getAmazonS3Client(): AmazonS3Client {
        val builder = AmazonS3ClientBuilder.standard()
        awsConfigurationProperties.s3EndPoint?.takeIf(String::isNotBlank)?.let {
            builder.withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    awsConfigurationProperties.s3EndPoint,
                    awsConfigurationProperties.region
                )
            )
        } ?: run {
            builder.withRegion(awsConfigurationProperties.region)
        }
        return builder.build() as AmazonS3Client
    }
}
