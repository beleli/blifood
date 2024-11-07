package br.com.blifood.infrastructure.storage

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.StorageException
import br.com.blifood.infrastructure.createImage
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File
import java.nio.file.Path

class LocalImageStorageAdapterTest : StringSpec({

    val testDir = Path.of("./catalog").toFile()
    val localImageStorageAdapter = LocalImageStorageAdapter(testDir.absolutePath)

    afterTest { testDir.listFiles()?.forEach { it.delete() } }

    "should upload image successfully" {
        val image = createImage()
        val imageFile = File(testDir, image.fileName)

        localImageStorageAdapter.upload(image)

        imageFile.exists() shouldBe true
        imageFile.readText() shouldBe "Test content"
    }

    "should throw StorageException when upload fails due to invalid directory" {
        val image = createImage()
        val invalidDir = File("./invalidDir")
        val localAdapterWithInvalidPath = LocalImageStorageAdapter(invalidDir.absolutePath)

        val exception = shouldThrow<StorageException> { localAdapterWithInvalidPath.upload(image) }
        exception.message shouldBe Messages.get("product-image.upload-exception")
    }

    "should remove image successfully" {
        val fileName = "test-image.jpg"
        val imageFile = File(testDir, fileName)
        imageFile.writeText("Test content")

        localImageStorageAdapter.remove(fileName)
        imageFile.exists() shouldBe false
    }

    "should recover image successfully" {
        val fileName = "test-image.jpg"
        val imageFile = File(testDir, fileName)
        imageFile.writeText("Test content")

        val inputStream = localImageStorageAdapter.recover(fileName)
        inputStream.bufferedReader().readText() shouldBe "Test content"
    }

    "isURL should return false" {
        localImageStorageAdapter.isURL() shouldBe false
    }
})
