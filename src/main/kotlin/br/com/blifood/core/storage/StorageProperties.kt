package br.com.blifood.core.storage

import com.amazonaws.regions.Regions
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("blifood.storage")
class StorageProperties(
    val type: Type,
    val local: Local?,
    val s3: S3?
) {
    init {
        when (type) {
            Type.S3 -> require((s3 != null)) { "blifood.storage.s3 configuration can not be null" }
            Type.LOCAL -> require((local != null)) { "blifood.storage.local configuration can not be null" }
        }
    }

    enum class Type {
        LOCAL,
        S3
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
