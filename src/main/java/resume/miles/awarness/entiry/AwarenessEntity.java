package resume.miles.awarness.entiry;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "awareness")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AwarenessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "awareness_name", nullable = false)
    private String awarenessName;

    @Column(nullable = true)
    private String image;

  
    @Column(nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "color_code", nullable = true)
    private String colorCode;

    
    @Column(name = "subsidebar_id")
    private Long subsidebarId;

    @Column(columnDefinition = "integer default 1")
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
