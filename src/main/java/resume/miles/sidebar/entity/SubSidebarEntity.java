package resume.miles.sidebar.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "subsidebars")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubSidebarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subsidebar_name")
    private String subSidebarName;

    @Column(name = "subsidebar_short_name")
    private String subSidebarShortName;

    
    @Column(name = "mastersidebar_id")
    private Long masterSidebar;

    @Column(name = "status")
    private Integer status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mastersidebar_id",nullable = false,insertable = false, updatable = false)
    @ToString.Exclude             
    @EqualsAndHashCode.Exclude
    private MasterSidebarEntity masterSidebarEntity;
}
