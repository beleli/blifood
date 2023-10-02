package br.com.blifood.core.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.util.unit.DataSize
import org.springframework.web.multipart.MultipartFile

class FileSizeValidator : ConstraintValidator<FileSize, MultipartFile?> {

    private var maxSize: DataSize? = null

    override fun initialize(constraintAnnotation: FileSize) {
        maxSize = DataSize.parse(constraintAnnotation.max)
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        return if (value == null) true else value.size <= maxSize!!.toBytes()
    }
}
