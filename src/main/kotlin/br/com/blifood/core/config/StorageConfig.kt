package br.com.blifood.core.config

import br.com.blifood.core.properties.StorageProperties
import br.com.blifood.domain.service.ImageStorageService
import br.com.blifood.infrastructure.storage.LocalImageStorageAdapter
import br.com.blifood.infrastructure.storage.S3ImageStorageAdapter
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfig(
    private val storageProperties: StorageProperties
) {

    @Bean
    @ConditionalOnProperty(name = ["blifood.storage.type"], havingValue = "s3")
    fun amazonS3(): AmazonS3 {
        val s3Properties = storageProperties.s3!!
        val credentials = BasicAWSCredentials(
            s3Properties.accessKey,
            s3Properties.secretAccessKey
        )

        return AmazonS3ClientBuilder.standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withRegion(s3Properties.region)
            .build()
    }

    @Bean
    fun imageStorageService(): ImageStorageService {
        return if (storageProperties.type == StorageProperties.Type.LOCAL) {
            val localProperties = storageProperties.local!!
            LocalImageStorageAdapter(localProperties.path)
        } else {
            val s3Properties = storageProperties.s3!!
            S3ImageStorageAdapter(amazonS3(), s3Properties.bucket, s3Properties.path)
        }
    }
}
