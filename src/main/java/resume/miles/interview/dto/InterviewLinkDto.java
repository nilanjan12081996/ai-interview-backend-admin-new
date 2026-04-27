package resume.miles.interview.dto;


import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewLinkDto {

    private Long id;
    private Long interviewId;

    private String token;

    private String interviewLink;

    private LocalDateTime expiryTime;
    private String terminationCause;
    private String userJustification;
    private Boolean isActive;
    private Integer coding;
    private Integer interviewChecking;
    private Long userId;
    private InterviewDto interviewDto; 
}
