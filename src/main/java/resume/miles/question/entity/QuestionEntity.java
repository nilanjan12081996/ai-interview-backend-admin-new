package resume.miles.question.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name = "questions")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionEntity {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
       @Column(name = "candidate_job_schedule_id")
    private Long candidateJobScheduleId;

    @Column(columnDefinition = "TEXT")
    private String questions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
