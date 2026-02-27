package resume.miles.interview.entity;

import java.time.LocalDate;
import java.time.LocalTime;
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
import resume.miles.jobs.entity.JobEntity;
import resume.miles.transcription.entity.TranscriptionEntity;
@Entity
@Table(name = "candidate_job_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewEntity extends BaseEntity {
        @Column(name = "job_id", nullable = false, length = 255)
    private String jobId;

    @Column(name = "candidate_name", nullable = false, length = 255)
    private String candidateName;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", nullable = false, columnDefinition = "LONGTEXT")
    private String phoneNumber;

    @Column(name = "resume_link", nullable = false, columnDefinition = "LONGTEXT")
    private String resumeLink;

    @Column(name = "is_coding", nullable = false)
    private Boolean isCoding;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "interview_date")
    private LocalDate interviewDate;

    @Column(name = "status", nullable = false)
    private Integer status = 1;
    

    @OneToMany(mappedBy = "interview",
           cascade = CascadeType.ALL,
           orphanRemoval = true)
            @OrderBy("id ASC")
    @BatchSize(size =50)
    private Set<InterviewLinkEntity> interviewEntity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false,updatable = false)
    private JobEntity jobEntity;
}
