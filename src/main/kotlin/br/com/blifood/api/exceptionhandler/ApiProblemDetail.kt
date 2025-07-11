package br.com.blifood.api.exceptionhandler

import br.com.blifood.core.log.Loggable
import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.net.URI

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ApiProblemDetail", description = "Details about an API error")
class ApiProblemDetail(

    @Schema(name = "title", example = "Bad Request")
    val title: String,

    @Schema(example = "400")
    val status: Int,

    @Schema(example = "Validation Failed")
    val detail: String,

    @Schema(example = "/v1/cities")
    val instance: URI,

    @Schema(example = "65e256b2eb1809d91a8313d412374fd6")
    val traceId: String?,

    @Schema(description = "List of objects or fields that generated the error")
    val errors: Set<ApiFieldError>? = null
) : Loggable {

    @Schema(name = "ApiFieldError", description = "Details about a specific field error")
    class ApiFieldError(

        @Schema(example = "name")
        val field: String,

        @Schema(example = "must not be blank")
        val error: String
    )
}
