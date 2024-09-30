package br.com.blifood.infrastructure

import br.com.blifood.domain.service.EmailService
import br.com.blifood.domain.service.ImageStorageService
import java.io.ByteArrayInputStream
import java.io.InputStream

fun createMessage(
    to: Set<String> = setOf("user@example.com"),
    subject: String = "Test Subject",
    template: String = "test-template",
    variables: Map<String, Any> = mapOf("key" to "value")
) = EmailService.Message(
    to = to,
    subject = subject,
    template = template,
    variables = variables
)

fun createImage(
    fileName: String = "test.jpg",
    contentType: String = "image/jpeg",
    inputStream: InputStream = ByteArrayInputStream("test".toByteArray())
) = ImageStorageService.Image(
    fileName = fileName,
    contentType = contentType,
    inputStream = inputStream
)
