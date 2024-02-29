package br.com.blifood.infrastructure.email

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.EmailException
import freemarker.template.Configuration
import freemarker.template.Template
import org.springframework.stereotype.Component
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils

@Component
class TemplateProcessor(
    private val freemarkerConfig: Configuration
) {

    fun processTemplate(templateName: String, variables: Map<String, Any>): String {
        return try {
            val template: Template = freemarkerConfig.getTemplate(templateName)
            FreeMarkerTemplateUtils.processTemplateIntoString(template, variables)
        } catch (cause: Throwable) {
            throw EmailException(Messages.get("email.templateException"), cause)
        }
    }
}
