package resume.miles.codingans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resume.miles.codingans.dto.CodingAnsDto;
import resume.miles.codingans.entity.CodingAnsEntity;

import java.util.Optional;

public interface CodingAnsRepository extends JpaRepository<CodingAnsEntity,Long> {
    Optional<CodingAnsEntity> findByToken(String token);

}
