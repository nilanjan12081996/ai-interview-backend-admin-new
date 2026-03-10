package resume.miles.resetpassword.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import resume.miles.resetpassword.annotation.PasswordMatch;
import resume.miles.resetpassword.dto.PasswordResetDto;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, PasswordResetDto> {

    @Override
    public boolean isValid(PasswordResetDto dto, ConstraintValidatorContext context) {
        // Handle null cases to avoid NullPointerException
        if (dto.getNewPassword() == null || dto.getConfirmPassword() == null) {
            return false;
        }

        boolean isValid = dto.getNewPassword().equals(dto.getConfirmPassword());

        // Optional: Attach the error message specifically to the confirmPassword field
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return isValid;
    }
}
