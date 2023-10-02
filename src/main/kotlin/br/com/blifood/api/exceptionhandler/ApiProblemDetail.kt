package br.com.blifood.api.exceptionhandler

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.net.URI

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ApiProblemDetail")
class ApiProblemDetail(

    @Schema(name = "title", example = "Bad Request")
    val title: String,

    @Schema(example = "400")
    val status: Int,

    @Schema(example = "Validation Failed")
    val detail: String,

    @Schema(example = "/v1/cities")
    val instance: URI,

    @Schema(description = "List of objects or fields that generated the error")
    val errors: Set<ApiFieldError>? = null
) {

    @Schema(name = "ApiFieldError")
    class ApiFieldError(

        @Schema(example = "name")
        val field: String,

        @Schema(example = "must not be blank")
        val error: String
    )
}
