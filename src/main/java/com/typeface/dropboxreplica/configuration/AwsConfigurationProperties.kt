package com.typeface.dropboxreplica.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated

@Validated
@ConfigurationProperties(prefix = "aws")
open class AwsConfigurationProperties {
    lateinit var region: String
    var awsEndPoint: String? = null
    var s3EndPoint: String? = null
    var accessKey: String? = null
    var secretKey: String? = null
}