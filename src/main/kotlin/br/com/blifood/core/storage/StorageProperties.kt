package br.com.blifood.core.storage

import com.amazonaws.regions.Regions
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blifood.storage")
data class StorageProperties(
    val type: StorageType,
    val local: Local?,
    val s3: S3?
) {
    init {
        when (type) {
            StorageType.S3 -> require((s3 != null)) { "blifood.storage.s3 configuration can not be null" }
            StorageType.LOCAL -> require((local != null)) { "blifood.storage.local configuration can not be null" }
        }
    }

    data class Local(
        val path: String
    )

    data class S3(
        val accessKey: String,
        val secretAccessKey: String,
        val bucket: String,
        val region: Regions,
        val path: String?
    )
}
