package resume.miles.codingans.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "coding_ans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodingAnsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    // Using String here for simplicity, but this can also be mapped
    // to a Jackson JsonNode or a Map<String, Object> depending on your needs.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ans", nullable = false, columnDefinition = "json")
    private String ans;

    @Builder.Default
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
