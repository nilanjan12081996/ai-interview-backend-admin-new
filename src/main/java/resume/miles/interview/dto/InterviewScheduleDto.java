package resume.miles.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterviewScheduleDto {

    private String jobId;
    private String candidateName;
    private String email;
    private String phoneNumber;
    private MultipartFile resumeFile;
    private Boolean isCoding;
    private String startTime;
    private String endTime;
    private String interviewDate;

    // 1. Accept them as Strings from the frontend

}
