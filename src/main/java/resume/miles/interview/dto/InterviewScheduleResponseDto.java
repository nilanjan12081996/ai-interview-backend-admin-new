package resume.miles.interview.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import resume.miles.users.dto.UserDto;

@Getter
@Builder
public class InterviewScheduleResponseDto {

    private Long id;
    private String jobId;

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
    private String videoLink;
    private String terminationCause;
    private String userJustification;
    private String transcription;
    private String duration;

    private String analysis;
    private Integer is_complete;

    private UserDto users;

}
