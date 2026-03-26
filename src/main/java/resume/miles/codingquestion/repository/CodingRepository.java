package resume.miles.codingquestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import resume.miles.codingquestion.entity.CodingEntity;

import java.util.Optional;

@Repository
public interface CodingRepository extends JpaRepository<CodingEntity, Long>, JpaSpecificationExecutor<CodingEntity> {

    Optional<CodingEntity> findByToken(String token);
}
