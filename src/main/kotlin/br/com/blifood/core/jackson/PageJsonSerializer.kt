package br.com.blifood.core.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.boot.jackson.JsonComponent
import org.springframework.data.domain.Page

@JsonComponent
class PageJsonSerializer : JsonSerializer<Page<Any>>() {

    override fun serialize(page: Page<Any>, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeObjectField("content", page.content)
        gen.writeNumberField("size", page.size)
        gen.writeNumberField("totalElements", page.totalElements)
        gen.writeNumberField("totalPages", page.totalPages)
        gen.writeNumberField("number", page.number)
        gen.writeEndObject()
    }
}
