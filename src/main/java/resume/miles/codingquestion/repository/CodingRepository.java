package resume.miles.codingquestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import resume.miles.codingquestion.entity.CodingEntity;

public interface CodingRepository extends JpaRepository<CodingEntity, Long>, JpaSpecificationExecutor<CodingEntity> {
}
