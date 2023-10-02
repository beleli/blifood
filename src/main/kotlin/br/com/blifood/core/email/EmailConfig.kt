package br.com.blifood.core.email

import br.com.blifood.domain.service.EmailService
import br.com.blifood.infrastructure.email.FakeEmailService
import br.com.blifood.infrastructure.email.SmtpEmailService
import br.com.blifood.infrastructure.email.TemplateProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class EmailConfig(
    private val emailProperties: EmailProperties,
    private val templateProcessor: TemplateProcessor,
) {

    @Bean
    fun emailService(): EmailService {
        return when (emailProperties.impl) {
            EmailImpl.FAKE -> FakeEmailService(templateProcessor)
            EmailImpl.SMTP -> SmtpEmailService(templateProcessor, JavaMailSenderImpl(), emailProperties.from)
            EmailImpl.SANDBOX -> SmtpEmailService(templateProcessor, JavaMailSenderImpl(), emailProperties.from, emailProperties.to!!)
        }
    }
}
