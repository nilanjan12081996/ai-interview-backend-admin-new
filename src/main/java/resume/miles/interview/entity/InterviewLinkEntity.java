package resume.miles.interview.entity;

import java.time.LocalDateTime;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.transcription.entity.TranscriptionEntity;

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
    @Column(name="is_complete",nullable = false)
    private Integer is_complete;

    @Column(name = "coding", nullable = false,columnDefinition = "TINYINT DEFAULT 0")
    private Integer coding;

    @Column(name = "interview", nullable = false,columnDefinition = "TINYINT DEFAULT 1")
    private Integer interviewChecking;

    @Column(name = "termination_cause")
    private String terminationCause;

    @Column(name = "user_justification", columnDefinition = "TEXT")
    private String userJustification;

    @OneToMany(mappedBy = "interviewDetails",
           cascade = CascadeType.ALL,
           orphanRemoval = true)
            @OrderBy("id ASC")
    @BatchSize(size =50)
    private Set<TranscriptionEntity> transcriptions;


}
