package resume.miles.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import resume.miles.blog.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity,Long>,JpaSpecificationExecutor<PostEntity>{
    
}
