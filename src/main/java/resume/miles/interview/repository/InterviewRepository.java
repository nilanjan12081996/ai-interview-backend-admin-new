package resume.miles.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import resume.miles.interview.entity.InterviewEntity;

public interface InterviewRepository extends JpaRepository<InterviewEntity,Long>{
    
}
