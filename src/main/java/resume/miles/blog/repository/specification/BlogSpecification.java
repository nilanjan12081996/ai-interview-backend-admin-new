package resume.miles.blog.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import resume.miles.blog.entity.PostEntity;


public class BlogSpecification {
    private BlogSpecification(){

    }
    public static Specification<PostEntity> joinTable(Long id,String slug){
        return (root, query, criteriaBuilder) -> {
          
            if (id != null) {
                return criteriaBuilder.equal(root.get("id"), id);
            }
            if(slug != null){
                return criteriaBuilder.equal(root.get("slug"), slug);
            }
            query.distinct(true);
            query.orderBy(criteriaBuilder.desc(root.get("id"))); 
            return criteriaBuilder.conjunction();
        };
        
    }
}
