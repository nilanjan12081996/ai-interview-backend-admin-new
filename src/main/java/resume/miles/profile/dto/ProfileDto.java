package resume.miles.profile.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileDto {
    private String fristName;
    private String lastName;
    private String email;
    private String mobile;
    private String gender;
}
