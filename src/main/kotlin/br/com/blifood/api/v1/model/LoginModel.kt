package br.com.blifood.api.v1.model

import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.Loggable.MaskProperty
import io.swagger.v3.oas.annotations.media.Schema

open class LoginModel(

    @MaskProperty
    @Schema(example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9==")
    val token: String
) : Loggable
