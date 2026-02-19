package resume.miles.interview.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InterviewScheduleResponseDto {

    private String candidateName;
    private String candidateEmail;
    private String candidatePhone;
    private String resumeLink;

    private String jobName;
    private String jobDescription;

    private LocalDate interviewDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private String interviewLink;
}
