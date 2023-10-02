package br.com.blifood.core.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile

class FileContentTypeValidator : ConstraintValidator<FileContentType, MultipartFile?> {

    private var allowedContentTypes: List<String> = listOf()

    override fun initialize(constraint: FileContentType) {
        allowedContentTypes = constraint.allowed.toList()
    }

    override fun isValid(multipartFile: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        return (multipartFile == null || allowedContentTypes.contains(multipartFile.contentType))
    }
}
