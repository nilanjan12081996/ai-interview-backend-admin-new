package resume.miles.interview.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "interview_link")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewLinkEntity extends BaseEntity {
      @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_id", nullable = false)
    private InterviewEntity interview;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "interview_link", nullable = false, columnDefinition = "LONGTEXT")
    private String interviewLink;

    @Column(name = "expiry_time", nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
