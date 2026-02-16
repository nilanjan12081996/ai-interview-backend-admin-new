package resume.miles.sidebar.entity;

import jakarta.persistence.*; 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import resume.miles.questions.entity.Answer;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "master_sidebars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterSidebarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sidebar_name")
    private String sidebarName;

    @Column(name = "sidebar_short_name")
    private String sidebarShortName;

  
    @Column(name = "status")
    private Integer status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "masterSidebarEntity")
    @ToString.Exclude              
    @EqualsAndHashCode.Exclude
    private Set<SubSidebarEntity> subSidebarEntity;
}
