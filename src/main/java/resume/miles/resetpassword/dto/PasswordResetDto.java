package resume.miles.resetpassword.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.resetpassword.annotation.PasswordMatch;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@PasswordMatch
public class PasswordResetDto {

    @NotBlank(message = "old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
