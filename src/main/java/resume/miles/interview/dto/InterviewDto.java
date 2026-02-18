package resume.miles.interview.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewDto {
    private Long id;
    @NotBlank(message = "Job ID is required")
    private String jobId;

    @NotBlank(message = "Candidate name is required")
    private String candidateName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Resume link is required")
    private String resumeLink;

    @NotNull(message = "Coding flag is required")
    private Boolean isCoding;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate interviewDate;

    private Integer status; // Optional (default 1)

    private LocalDateTime createdAt;  // ✅ add this
    private LocalDateTime updatedAt;  // ✅ add this
}
