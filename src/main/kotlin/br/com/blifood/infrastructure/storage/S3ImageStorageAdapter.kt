package br.com.blifood.infrastructure.storage

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.StorageException
import br.com.blifood.domain.service.ImageStorageService
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import java.net.URL

class S3ImageStorageAdapter(
    private val amazonS3: AmazonS3,
    private val bucket: String,
    private val path: String?
) : ImageStorageService {

    override fun upload(image: ImageStorageService.Image) {
        runCatching {
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentType = image.contentType
            val putObjectRequest = PutObjectRequest(
                bucket,
                fullPath(image.fileName),
                image.inputStream,
                objectMetadata
            ).withCannedAcl(CannedAccessControlList.PublicRead)
            amazonS3.putObject(putObjectRequest)
        }.onFailure {
            throw StorageException(Messages.get("product-image.upload-exception"), it)
        }
    }

    override fun remove(fileName: String) {
        runCatching {
            val deleteObjectRequest = DeleteObjectRequest(bucket, fullPath(fileName))
            amazonS3.deleteObject(deleteObjectRequest)
        }.onFailure {
            throw StorageException(Messages.get("product-image.remove-exception"), it)
        }
    }

    override fun recover(fileName: String): URL {
        return amazonS3.getUrl(bucket, fullPath(fileName))
    }

    override fun isURL() = true

    private fun fullPath(fileName: String) = if (path == null) fileName else "$path/$fileName"
}
