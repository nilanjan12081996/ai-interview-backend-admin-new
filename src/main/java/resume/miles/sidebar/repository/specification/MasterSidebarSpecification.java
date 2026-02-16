package resume.miles.sidebar.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import resume.miles.sidebar.entity.MasterSidebarEntity;



public class MasterSidebarSpecification {
    public MasterSidebarSpecification(){

    }
    public static Specification<MasterSidebarEntity> joinTable(Long id){
                return (root,query,criteriaBuilder)->{
                    if (Long.class != query.getResultType()) {
                       root.fetch("subSidebarEntity", JoinType.LEFT);
                    }
                    if (id != null) {
                       return criteriaBuilder.equal(root.get("id"), id);
                    }
                    query.distinct(true);
                    query.orderBy(criteriaBuilder.asc(root.get("id"))); 
                    return criteriaBuilder.conjunction();
                };
    }
}
