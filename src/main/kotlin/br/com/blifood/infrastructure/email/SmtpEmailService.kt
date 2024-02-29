package br.com.blifood.infrastructure.email

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.EmailException
import br.com.blifood.domain.service.EmailService
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

open class SmtpEmailService(
    private val templateProcessor: TemplateProcessor,
    private val mailSender: JavaMailSender,
    private val from: String,
    private val to: Set<String>? = null
) : EmailService {

    override fun send(message: EmailService.Message) {
        val mimeMessage = createMimeMessage(message)
        runCatching {
            mailSender.send(mimeMessage)
        }.onFailure {
            throw EmailException(Messages.get("email.sendException"), it)
        }
    }

    private fun createMimeMessage(message: EmailService.Message): MimeMessage {
        val body = templateProcessor.processTemplate(message.template, message.variables)
        val mimeMessage = mailSender.createMimeMessage()

        val helper = MimeMessageHelper(mimeMessage, "UTF-8")
        helper.setFrom(from)
        helper.setTo(message.to.toTypedArray())
        helper.setSubject(message.subject)
        helper.setText(body, true)

        to?.let { helper.setTo(it.toTypedArray()) }

        return mimeMessage
    }
}
