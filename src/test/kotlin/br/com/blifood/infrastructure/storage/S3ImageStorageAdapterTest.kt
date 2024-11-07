package br.com.blifood.infrastructure.storage

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.StorageException
import br.com.blifood.infrastructure.createImage
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import java.net.URL

class S3ImageStorageAdapterTest : StringSpec({

    val amazonS3 = mockk<AmazonS3>(relaxed = true)
    val bucket = "test-bucket"
    val path = "images"
    val s3ImageStorageAdapter = S3ImageStorageAdapter(amazonS3, bucket, path)

    "should upload image successfully" {
        val image = createImage()
        mockkConstructor(ObjectMetadata::class)
        every { anyConstructed<ObjectMetadata>().contentType = image.contentType } just Runs

        s3ImageStorageAdapter.upload(image)
    }

    "should throw StorageException when upload fails" {
        val image = createImage()
        mockkConstructor(ObjectMetadata::class)
        every { amazonS3.putObject(any<PutObjectRequest>()) } throws RuntimeException("Failed to upload")

        val exception = shouldThrow<StorageException> { s3ImageStorageAdapter.upload(image) }
        exception.message shouldBe Messages.get("product-image.upload-exception")
    }

    "should remove image successfully" {
        val fileName = "image.jpg"
        every { amazonS3.deleteObject(any<DeleteObjectRequest>()) } just Runs

        s3ImageStorageAdapter.remove(fileName)
    }

    "should throw StorageException when remove fails" {
        val fileName = "image.jpg"
        every { amazonS3.deleteObject(any<DeleteObjectRequest>()) } throws RuntimeException("Failed to delete")

        val exception = shouldThrow<StorageException> { s3ImageStorageAdapter.remove(fileName) }
        exception.message shouldBe Messages.get("product-image.remove-exception")
    }

    "should recover image successfully" {
        val fileName = "image.jpg"
        val url = URL("https://s3.amazonaws.com/$bucket/$path/$fileName")
        every { amazonS3.getUrl(bucket, "$path/$fileName") } returns url

        val result = s3ImageStorageAdapter.recover(fileName)
        result shouldBe url
    }

    "isURL should return true" {
        s3ImageStorageAdapter.isURL() shouldBe true
    }
})
