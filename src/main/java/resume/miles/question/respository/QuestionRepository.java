package resume.miles.question.respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import resume.miles.question.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity,Long>{
        List<QuestionEntity> findByCandidateJobScheduleId(Long candidateJobScheduleId);
}
