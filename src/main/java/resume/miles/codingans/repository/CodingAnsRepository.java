package resume.miles.codingans.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import resume.miles.codingans.dto.CodingAnsDto;
import resume.miles.codingans.entity.CodingAnsEntity;

import java.util.List;
import java.util.Optional;

public interface CodingAnsRepository extends JpaRepository<CodingAnsEntity,Long> {
    Optional<CodingAnsEntity> findByToken(String token);
    @Query("SELECT c FROM CodingAnsEntity c WHERE c.token = :token")
    List<CodingAnsEntity> findAllAnswersByToken(@Param("token") String token);
}
