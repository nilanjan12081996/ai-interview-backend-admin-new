package resume.miles.interview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import resume.miles.interview.entity.InterviewEntity;

public interface InterviewRepository extends JpaRepository<InterviewEntity,Long>{
    // List<InterviewEntity> findByJob_Id(Long jobId);
     List<InterviewEntity> findByJobId(String jobId);

     long countByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
     
     List<InterviewEntity> findByInterviewDate(java.time.LocalDate date);
     
     long countByCreatedAtAfter(java.time.LocalDateTime date);

     long countByCreatedAtBefore(java.time.LocalDateTime date);

     List<InterviewEntity> findTop10ByOrderByCreatedAtDesc();
}
