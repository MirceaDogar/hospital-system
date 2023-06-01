package ro.hospitalmanagement.system.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NoDigitsValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoDigits {
    String message() default "Can not contain digits!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}