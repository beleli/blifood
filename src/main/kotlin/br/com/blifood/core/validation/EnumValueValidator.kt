package br.com.blifood.core.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

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
