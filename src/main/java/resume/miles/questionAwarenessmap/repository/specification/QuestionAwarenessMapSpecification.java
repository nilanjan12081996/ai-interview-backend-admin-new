package resume.miles.questionAwarenessmap.repository.specification;



import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import resume.miles.questionAwarenessmap.entity.AwarenessQuestionMap;
import resume.miles.questions.entity.Question;

public class QuestionAwarenessMapSpecification {
     private QuestionAwarenessMapSpecification(){

     }

     public static Specification<AwarenessQuestionMap> withQuestionData(){
          return (root,query,criteriaBuilder)->{
                    if(Long.class != query.getResultType()){
                       Fetch<AwarenessQuestionMap, Question> questionJoin =  root.fetch("question", JoinType.LEFT);
                          questionJoin.fetch("answers",JoinType.LEFT);
                       
                    }
                    query.distinct(true);
                    
                    return criteriaBuilder.conjunction();
          };
     }
}
