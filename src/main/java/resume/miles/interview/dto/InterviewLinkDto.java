package resume.miles.interview.dto;


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

    private Boolean isActive;

    private InterviewDto interviewDto; 
}
