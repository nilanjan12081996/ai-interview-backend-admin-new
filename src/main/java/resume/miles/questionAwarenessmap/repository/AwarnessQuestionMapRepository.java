package resume.miles.questionAwarenessmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import resume.miles.questionAwarenessmap.entity.AwarenessQuestionMap;
import java.util.List;


public interface AwarnessQuestionMapRepository extends JpaRepository<AwarenessQuestionMap,Long>,JpaSpecificationExecutor<AwarenessQuestionMap>{
        Long countByAwarenessId(Long id);
        Long countByQuestionIdAndAwarenessId(Long qid,Long aid);
}
