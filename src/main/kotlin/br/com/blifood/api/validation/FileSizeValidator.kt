package br.com.blifood.api.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.util.unit.DataSize
import org.springframework.web.multipart.MultipartFile
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [FileSizeValidator::class])
annotation class FileSize(
    val message: String = "fileSize.invalid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val max: String = "1MB"
)

class FileSizeValidator : ConstraintValidator<FileSize, MultipartFile?> {

    private var maxSize: DataSize? = null

    override fun initialize(constraintAnnotation: FileSize) {
        maxSize = DataSize.parse(constraintAnnotation.max)
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        return if (value == null) true else value.size <= maxSize!!.toBytes()
    }
}
