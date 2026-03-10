package resume.miles.resetpassword.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import resume.miles.resetpassword.validation.PasswordMatchValidator;

import java.lang.annotation.*;

@Target({ElementType.TYPE}) // Applied to the class, not a field
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch {
    String message() default "new password and confirm password do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
