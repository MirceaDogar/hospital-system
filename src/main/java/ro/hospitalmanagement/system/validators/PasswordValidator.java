package ro.hospitalmanagement.system.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public void initialize(Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {

        boolean isValid = false;
        if (password == null) {
            return true;
        }
        if (password.length() >= 10) {
            for (char c : password.toCharArray()) {
                if (Character.isDigit(c)) {
                    isValid = true;
                    break;
                }
            }
            if (isValid) {
                for (char c : password.toCharArray()) {
                    if (Character.isLetter(c)) {
                        break;
                    }
                }
            }
        }
        return isValid;
    }
}
