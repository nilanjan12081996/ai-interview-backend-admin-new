package resume.miles.interview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import resume.miles.interview.entity.InterviewEntity;

public interface InterviewRepository extends JpaRepository<InterviewEntity,Long>{
    // List<InterviewEntity> findByJob_Id(Long jobId);
}
