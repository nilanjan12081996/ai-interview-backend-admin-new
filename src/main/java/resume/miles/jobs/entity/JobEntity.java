package resume.miles.jobs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.client.entity.ClientEntity;
import resume.miles.config.baseclass.BaseEntity;

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

    // 🔥 LONGTEXT mapping
    @Lob
    @Column(name = "jd", nullable = false, columnDefinition = "LONGTEXT")
    private String jd;
}
