package greencity.validator;

import greencity.annotations.ValidStringLength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StringLengthValidator implements ConstraintValidator<ValidStringLength, String> {

    private int min;
    private int max;
    private boolean excludeHtml;

    @Override
    public void initialize(ValidStringLength constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.excludeHtml = constraintAnnotation.excludeHtml();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        String plainText = value;
        // Видалення HTML тегів
        if (excludeHtml) {
            plainText = value.replaceAll("<[^>]*>", "");
        }

        int length = plainText.length();
        return length >= min && length <= max;
    }
}
