package resume.miles.codingquestion.specification;

import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import resume.miles.interview.entity.InterviewLinkEntity;
import resume.miles.interview.entity.InterviewEntity;
import resume.miles.jobs.entity.JobEntity;

import java.util.ArrayList;
import java.util.List;

public class CodeGenerateFindSpecification {

    private CodeGenerateFindSpecification() {}

    public static Specification<InterviewLinkEntity> getFullInterviewDetailsByToken(String token) {
        return (root, query, criteriaBuilder) -> {

            if (Long.class != query.getResultType() && long.class != query.getResultType()) {
                Fetch<InterviewLinkEntity, InterviewEntity> interviewFetch = root.fetch("interview", JoinType.LEFT);
                Fetch<InterviewEntity, JobEntity> jobFetch = interviewFetch.fetch("jobEntity", JoinType.LEFT);

                jobFetch.fetch("client", JoinType.LEFT);
                jobFetch.fetch("mandatorySkills", JoinType.LEFT);
                jobFetch.fetch("mustHaveSkills", JoinType.LEFT);

                query.distinct(true);
            }
            List<Predicate> predicates = new ArrayList<>();
            if (token != null && !token.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("token"), token));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}