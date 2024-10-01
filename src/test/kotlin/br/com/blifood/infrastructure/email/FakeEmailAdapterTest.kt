package br.com.blifood.infrastructure.email

import br.com.blifood.domain.service.EmailService
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class FakeEmailAdapterTest : StringSpec({

    val templateProcessor: TemplateProcessor = mockk()
    val fakeEmailAdapter = FakeEmailAdapter(templateProcessor)

    "should log and process template correctly when sending email" {
        val message = EmailService.Message(
            to = setOf("test@example.com"),
            subject = "test",
            template = "test-template",
            variables = mapOf("key" to "value")
        )
        every { templateProcessor.processTemplate(message.template, message.variables) } returns "Processed email body"

        fakeEmailAdapter.send(message)

        verify { templateProcessor.processTemplate(message.template, message.variables) }
    }
})
