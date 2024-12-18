package br.com.blifood.infrastructure.storage

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.StorageException
import br.com.blifood.domain.service.ImageStorageService
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

class LocalImageStorageAdapter(
    private val path: String
) : ImageStorageService {

    override fun upload(image: ImageStorageService.Image) {
        runCatching {
            val path = Path.of(path, image.fileName)
            image.inputStream.copyTo(Files.newOutputStream(path))
        }.onFailure {
            throw StorageException(Messages.get("product-image.upload-exception"), it)
        }
    }

    override fun remove(fileName: String) {
        runCatching {
            val path = Path.of(path, fileName)
            Files.deleteIfExists(path)
        }.onFailure {
            throw StorageException(Messages.get("product-image.remove-exception"), it)
        }
    }

    override fun recover(fileName: String): InputStream {
        val path = Path.of(path, fileName)
        return Files.newInputStream(path)
    }

    override fun isURL() = false
}
