package br.com.blifood.api.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.web.multipart.MultipartFile
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FileContentTypeValidator::class])
annotation class FileContentType(
    val message: String = "fileContentType.invalid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val allowed: Array<String>
)

class FileContentTypeValidator : ConstraintValidator<FileContentType, MultipartFile?> {

    private val allowedContentTypes = mutableSetOf<String>()

    override fun initialize(constraint: FileContentType) {
        allowedContentTypes.addAll(constraint.allowed.toSet())
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        return value?.let { allowedContentTypes.contains(it.contentType) } ?: true
    }
}
