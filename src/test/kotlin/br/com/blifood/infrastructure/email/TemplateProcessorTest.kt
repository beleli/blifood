package br.com.blifood.infrastructure.email

import br.com.blifood.core.message.Messages
import br.com.blifood.domain.exception.EmailException
import freemarker.template.Configuration
import freemarker.template.Template
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils

class TemplateProcessorTest : StringSpec({

    val freemarkerConfig = mockk<Configuration>()
    val template = mockk<Template>()
    val templateProcessor = TemplateProcessor(freemarkerConfig)

    val templateName = "test-template"
    val variables = mapOf("key" to "value")

    "should process template successfully" {
        val processedTemplate = "Processed template"
        every { freemarkerConfig.getTemplate(templateName) } returns template
        mockkStatic(FreeMarkerTemplateUtils::class)
        every { FreeMarkerTemplateUtils.processTemplateIntoString(template, variables) } returns processedTemplate

        val result = templateProcessor.processTemplate(templateName, variables)

        result shouldBe processedTemplate
        verify { freemarkerConfig.getTemplate(templateName) }
        verify { FreeMarkerTemplateUtils.processTemplateIntoString(template, variables) }
    }

    "should throw EmailException when template processing fails" {
        every { freemarkerConfig.getTemplate(templateName) } throws RuntimeException("Template not found")

        val exception = shouldThrow<EmailException> { templateProcessor.processTemplate(templateName, variables) }

        exception shouldBe EmailException(Messages.get("email.templateException"))
    }
})
