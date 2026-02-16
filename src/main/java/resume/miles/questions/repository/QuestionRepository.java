package resume.miles.questions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import resume.miles.questions.entity.Question;

public interface QuestionRepository extends JpaRepository<Question,Long>,JpaSpecificationExecutor<Question>{
    Long countById(Long id);
}
