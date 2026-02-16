package resume.miles.questions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import resume.miles.questions.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer,Long>,JpaSpecificationExecutor<Answer>{
    
}
