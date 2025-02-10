package com.typeface.dropboxreplica;

import com.typeface.dropboxreplica.configuration.AwsConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {"com.typeface"})
@EnableJpaAuditing
@EnableConfigurationProperties(AwsConfigurationProperties.class)
public class DropboxReplicaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DropboxReplicaApplication.class, args);

	}

}
