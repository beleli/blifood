package br.com.blifood.api.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Constraint(validatedBy = [EnumValueValidator::class])
annotation class EnumValue(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "enumValue.invalid"
)

class EnumValueValidator : ConstraintValidator<EnumValue, String> {

    private val allowedValues = mutableSetOf<String>()

    override fun initialize(constraint: EnumValue) {
        constraint.enumClass.java.enumConstants.forEach {
            allowedValues.add(it.name.uppercase())
        }
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        return (value == null || allowedValues.contains(value.uppercase()))
    }
}
