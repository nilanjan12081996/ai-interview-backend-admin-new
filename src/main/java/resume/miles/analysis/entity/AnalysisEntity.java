package resume.miles.analysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;
import resume.miles.interview.entity.InterviewLinkEntity;

@Entity
@Table(name="analysis")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AnalysisEntity extends BaseEntity{
     
      @Column(name = "interview_link_id", nullable = false)
    private Long interviewLinkId;

    @Column(name = "analysis", nullable = false, columnDefinition = "LONGTEXT")
    private String analysis;

    @Column(name = "status", nullable = false)
    private Integer status;
       @Column(name = "duration", length = 50)
      private String duration;
     @ManyToOne(fetch = FetchType.LAZY)
      @JoinColumn(name = "interview_link_id", nullable = false,insertable = false,updatable = false)
      private InterviewLinkEntity interviewDetails;
}
