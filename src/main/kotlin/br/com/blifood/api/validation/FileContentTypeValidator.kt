package br.com.blifood.api.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FileContentTypeValidator::class])
annotation class FileContentType(
    val message: String = "fileContentType.invalid",
    val allowed: Array<String>
)

class FileContentTypeValidator : ConstraintValidator<FileContentType, MultipartFile?> {

    private val allowedContentTypes = mutableSetOf<String>()

    override fun initialize(constraint: FileContentType) {
        allowedContentTypes.addAll(constraint.allowed.toSet())
    }

    override fun isValid(multipartFile: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        return (multipartFile == null || allowedContentTypes.contains(multipartFile.contentType))
    }
}
