package br.com.blifood.infrastructure.email

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.EmailException
import br.com.blifood.infrastructure.createMessage
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender

class SmtpEmailAdapterTest : StringSpec({

    val templateProcessor = mockk<TemplateProcessor>(relaxed = true)
    val mailSender = mockk<JavaMailSender>(relaxed = true)
    val mimeMessage = mockk<MimeMessage>(relaxed = true)
    val from = "noreply@blifood.com"
    val toSet = setOf("admin@blifood.com")
    val smtpEmailAdapter = SmtpEmailAdapter(templateProcessor, mailSender, from, toSet)

    "should send email successfully" {
        val message = createMessage()
        every { templateProcessor.processTemplate(any(), any()) } returns "Processed email body"
        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(any<MimeMessage>()) } just Runs

        smtpEmailAdapter.send(message)

        verify { templateProcessor.processTemplate(message.template, message.variables) }
        verify { mailSender.send(mimeMessage) }
    }

    "should throw EmailException when sending email fails" {
        val message = createMessage()
        every { templateProcessor.processTemplate(message.template, message.variables) } returns "Processed email body"
        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(any<MimeMessage>()) } throws RuntimeException("Failed to send email")

        val exception = shouldThrow<EmailException> { smtpEmailAdapter.send(message) }

        exception shouldBe EmailException(Messages.get("email.sendException"))
        verify { mailSender.send(mimeMessage) }
    }
})
