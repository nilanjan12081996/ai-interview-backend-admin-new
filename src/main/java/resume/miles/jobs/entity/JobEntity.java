package resume.miles.jobs.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.client.entity.ClientEntity;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.interview.entity.InterviewEntity;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.mandatoryskill.entity.MandatorySkillEntity;
import resume.miles.musthaveskill.entity.MustHaveSkillEntity;
import resume.miles.jobs.enums.JobLevel;

@Entity
@Table(name = "job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobEntity extends BaseEntity{
      @Column(name = "job_id", nullable = false, unique = true)
    private String jobId;

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name="status",nullable = false)
    private Integer status;
    // 🔥 LONGTEXT mapping
    @Lob
    @Column(name = "jd", nullable = false, columnDefinition = "LONGTEXT")
    private String jd;
    @Column(name = "experience", length = 50)
    private String experience;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", length = 20)
    private JobLevel level;

    @OneToMany(mappedBy = "jobDetails",
           cascade = CascadeType.ALL,
           orphanRemoval = true)
    @OrderBy("id ASC")
    @BatchSize(size =50)
private Set<MandatorySkillEntity> mandatorySkills;

@OneToMany(mappedBy = "job",
           cascade = CascadeType.ALL,
           orphanRemoval = true)
            @OrderBy("id ASC")
            @BatchSize(size =50)
            private Set<MustHaveSkillEntity> mustHaveSkills;

   @OneToMany(mappedBy = "jobEntity",
           cascade = CascadeType.ALL,
           orphanRemoval = true)
            @OrderBy("id ASC")
    @BatchSize(size =50)
    private Set<InterviewEntity> interviewEntity;
}

