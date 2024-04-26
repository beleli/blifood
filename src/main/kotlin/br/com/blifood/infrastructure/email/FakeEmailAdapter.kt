package br.com.blifood.infrastructure.email

import br.com.blifood.domain.service.EmailService
import org.slf4j.LoggerFactory

class FakeEmailAdapter(
    private val templateProcessor: TemplateProcessor
) : EmailService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun send(message: EmailService.Message) {
        val body = templateProcessor.processTemplate(message.template, message.variables)
        logger.info("[FAKE E-MAIL] To: ${message.to}\n$body")
    }
}
