package resume.miles.questions.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import resume.miles.questions.entity.Question;

public class QuestionSpecification {
    private QuestionSpecification(){

    }
    public static Specification<Question> joinTable(Long id){
        return (root, query, criteriaBuilder) -> {
            if (Long.class != query.getResultType()) {
                root.fetch("answers", JoinType.LEFT);
            }
            if (id != null) {
                return criteriaBuilder.equal(root.get("id"), id);
            }
            query.distinct(true);
            query.orderBy(criteriaBuilder.desc(root.get("id"))); 
            return criteriaBuilder.conjunction();
        };
        
    }
}
