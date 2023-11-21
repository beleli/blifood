package br.com.blifood.api.v1.model

import io.swagger.v3.oas.annotations.media.Schema

open class LoginModel(

    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9==")
    val token: String
)
