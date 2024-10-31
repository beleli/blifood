package br.com.blifood.api.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Constraint(validatedBy = [NumericValidator::class])
annotation class NumericValue(
    val message: String = "numericValue.invalid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class NumericValidator : ConstraintValidator<NumericValue, String> {

    private val regex = "^[0-9]+(\\.[0-9]+)?$".toRegex()

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value?.let { regex.matches(it) } ?: true
    }
}
