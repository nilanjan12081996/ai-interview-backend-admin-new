package resume.miles.codingans.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import resume.miles.codingans.dto.CodingAnsDto;
import resume.miles.codingans.entity.CodingAnsEntity;

public interface CodingAnsRepository extends JpaRepository<CodingAnsEntity,Long> {


}
