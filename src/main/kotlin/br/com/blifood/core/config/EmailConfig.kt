package br.com.blifood.core.config

import br.com.blifood.core.properties.EmailProperties
import br.com.blifood.domain.service.EmailService
import br.com.blifood.infrastructure.email.FakeEmailAdapter
import br.com.blifood.infrastructure.email.SmtpEmailAdapter
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
            EmailProperties.Impl.FAKE -> FakeEmailAdapter(templateProcessor)
            EmailProperties.Impl.SMTP -> SmtpEmailAdapter(templateProcessor, JavaMailSenderImpl(), emailProperties.from)
            EmailProperties.Impl.SANDBOX -> SmtpEmailAdapter(templateProcessor, JavaMailSenderImpl(), emailProperties.from, emailProperties.to!!)
        }
    }
}
