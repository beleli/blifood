package br.com.blifood.api.v1.model.input

import br.com.blifood.api.validation.FileContentType
import br.com.blifood.api.validation.FileSize
import br.com.blifood.core.log.Loggable
import br.com.blifood.core.log.Loggable.MaskProperty
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.entity.ProductImage
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile

data class ProductImageInputModel(

    @Schema(description = "Product photo file (maximum 500KB, JPG and PNG only)")
    @field:NotNull(message = "constraints.not-null")
    @field:FileContentType(allowed = [MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE])
    @field:FileSize(max = "500KB")
    val file: MultipartFile?,

    @Schema(example = "Foto do produto")
    @MaskProperty
    @field:NotBlank(message = "constraints.not-blank")
    @field:Size(max = 150, message = "constraints.size")
    val description: String?
) : Loggable

fun ProductImageInputModel.toEntity(product: Product) = ProductImage(
    id = product.id,
    fileName = "${product.id}.${file!!.originalFilename!!.substringAfter('.')}",
    description = description,
    contentType = file.contentType!!,
    size = file.size
)
