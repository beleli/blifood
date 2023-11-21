package br.com.blifood.core.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile

class FileContentTypeValidator : ConstraintValidator<FileContentType, MultipartFile?> {

    private val allowedContentTypes = mutableSetOf<String>()

    override fun initialize(constraint: FileContentType) {
        allowedContentTypes.addAll(constraint.allowed.toSet())
    }

    override fun isValid(multipartFile: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        return (multipartFile == null || allowedContentTypes.contains(multipartFile.contentType))
    }
}
