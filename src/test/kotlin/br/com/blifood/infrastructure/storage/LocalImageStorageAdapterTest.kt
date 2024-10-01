package br.com.blifood.infrastructure.storage

import br.com.blifood.domain.exception.StorageException
import br.com.blifood.infrastructure.createImage
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.verify
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Path

class LocalImageStorageAdapterTest : StringSpec({

    val path = "test-path"
    val localImageStorageAdapter = LocalImageStorageAdapter(path)
    beforeTest { mockkStatic(Files::class) }

    "upload should upload image to local storage" {
        val image = createImage()
        val filePath = Path.of(path, image.fileName)
        every { Files.exists(filePath) } returns false
        every { Files.createFile(filePath) } returns filePath
        every { Files.newOutputStream(filePath) } returns ByteArrayOutputStream()

        localImageStorageAdapter.upload(image)
    }

    "upload should throw StorageException on failure" {
        val image = createImage()
        val invalidPath = Path.of("test", image.fileName)

        every { Files.createFile(invalidPath) } throws RuntimeException()

        shouldThrow<StorageException> { localImageStorageAdapter.upload(image) }
    }

    "remove should delete image from local storage" {
        val image = createImage()
        val filePath = Path.of(path, image.fileName)
        every { Files.deleteIfExists(filePath) } returns true

        localImageStorageAdapter.remove(image.fileName)

        verify { Files.deleteIfExists(filePath) }
    }

    "remove should throw StorageException on failure" {
        val image = createImage()
        val invalidPath = Path.of("test", image.fileName)

        every { Files.delete(invalidPath) } throws RuntimeException()

        shouldThrow<StorageException> { localImageStorageAdapter.remove(image.fileName) }
    }

    "recover should return InputStream of the image" {
        val image = createImage()
        val filePath = Path.of(path, image.fileName)
        val inputStream = ByteArrayInputStream("test".toByteArray())
        every { Files.exists(filePath) } returns true
        every { Files.newInputStream(filePath) } returns inputStream

        val result = localImageStorageAdapter.recover(image.fileName)

        result shouldBe inputStream
    }
})
