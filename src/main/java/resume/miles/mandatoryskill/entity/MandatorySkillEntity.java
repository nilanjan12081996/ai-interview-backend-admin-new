package resume.miles.mandatoryskill.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.jobs.entity.JobEntity;

@Entity
@Table(name = "mandatory_skill")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MandatorySkillEntity extends BaseEntity{

     @Column(name = "job_id")
    private Long job;

    @Lob
    @Column(name = "skill_name")
    private String skillName;

    private Integer status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false,insertable = false,updatable = false)
    private JobEntity jobDetails;

}
