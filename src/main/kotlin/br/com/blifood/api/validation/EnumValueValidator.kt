package br.com.blifood.api.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Constraint(validatedBy = [EnumValueValidator::class])
annotation class EnumValue(
    val message: String = "validation.enum-value.invalid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val enumClass: KClass<out Enum<*>>
)

class EnumValueValidator : ConstraintValidator<EnumValue, String> {

    private val allowedValues = mutableSetOf<String>()

    override fun initialize(constraint: EnumValue) {
        constraint.enumClass.java.enumConstants.forEach {
            allowedValues.add(it.name.uppercase())
        }
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return value?.let { allowedValues.contains(it.uppercase()) } ?: true
    }
}
