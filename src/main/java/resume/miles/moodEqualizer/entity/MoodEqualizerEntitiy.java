package resume.miles.moodEqualizer.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Entity
@Table(name = "moodequalizers")
@Data 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoodEqualizerEntitiy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "awareness_id")
    private Long awarenessId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = true)
    private String name;

    @Column(nullable = false)
    private String type;

    // Default value logic: initialized to 1 to match DB default
    @Column(name = "status")
    @Builder.Default
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
