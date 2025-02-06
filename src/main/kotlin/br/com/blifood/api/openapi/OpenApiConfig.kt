package br.com.blifood.api.openapi

import br.com.blifood.api.exceptionhandler.ApiProblemDetail
import br.com.blifood.api.exceptionhandler.ApiProblemDetail.ApiFieldError
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.tags.Tag
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import java.util.function.Consumer

@Configuration
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class OpenApiConfig {

    private val clientErrorResponse = "ClientErrorResponse"

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(generateInfo())
            .tags(generateTags())
            .components(
                Components()
                    .schemas(generateSchemas())
                    .responses(generateResponses())
            )
            .addSecurityItem(SecurityRequirement().addList("Bearer Authentication"))
    }

    private fun generateInfo(): Info {
        return Info()
            .title("BliFood API")
            .version("1.0.0")
            .description("REST API for BliFood")
    }

    private fun generateTags(): List<Tag> {
        return listOf(
            Tag().name("Login").description("Authentication Token"),
            Tag().name("States").description("State Management"),
            Tag().name("Cities").description("City Management"),
            Tag().name("Culinary").description("Culinary Management"),
            Tag().name("PaymentMethods").description("Payment Methods Management"),
            Tag().name("Restaurants").description("Restaurants Management"),
            Tag().name("Products").description("Products Management"),
            Tag().name("Orders").description("Orders Management"),
            Tag().name("Users").description("Users Management")
        )
    }

    private fun generateSchemas(): Map<String, Schema<Any>> {
        val schemaMap = hashMapOf<String, Schema<Any>>()
        schemaMap.putAll(ModelConverters.getInstance().read(ApiProblemDetail::class.java))
        schemaMap.putAll(ModelConverters.getInstance().read(ApiFieldError::class.java))
        return schemaMap
    }

    @Bean
    fun openApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi ->
            openApi.paths.values.forEach(
                Consumer { pathItem ->
                    pathItem.readOperations().forEach { operation: Operation ->
                        operation.responses.addApiResponse("4xx", ApiResponse().`$ref`(clientErrorResponse))
                    }
                }
            )
        }
    }

    private fun generateResponses(): Map<String, ApiResponse> {
        val apiResponseMap = mutableMapOf<String, ApiResponse>()
        val content = Content().addMediaType(APPLICATION_JSON_VALUE, MediaType().schema(Schema<ApiProblemDetail>().`$ref`("ApiProblemDetail")))

        apiResponseMap[clientErrorResponse] = ApiResponse().description("Default api error response").content(content)

        return apiResponseMap
    }
}
