package br.com.blifood.core.config

import br.com.blifood.core.properties.EmailProperties
import br.com.blifood.domain.service.EmailService
import br.com.blifood.infrastructure.email.FakeEmailService
import br.com.blifood.infrastructure.email.SmtpEmailService
import br.com.blifood.infrastructure.email.TemplateProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class EmailConfig(
    private val emailProperties: EmailProperties,
    private val templateProcessor: TemplateProcessor
) {

    @Bean
    fun emailService(): EmailService {
        return when (emailProperties.impl) {
            EmailProperties.Impl.FAKE -> FakeEmailService(templateProcessor)
            EmailProperties.Impl.SMTP -> SmtpEmailService(templateProcessor, JavaMailSenderImpl(), emailProperties.from)
            EmailProperties.Impl.SANDBOX -> SmtpEmailService(templateProcessor, JavaMailSenderImpl(), emailProperties.from, emailProperties.to!!)
        }
    }
}
