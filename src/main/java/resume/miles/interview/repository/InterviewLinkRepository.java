package resume.miles.interview.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import resume.miles.interview.entity.InterviewLinkEntity;

public interface InterviewLinkRepository extends JpaRepository<InterviewLinkEntity, Long> {

    Optional<InterviewLinkEntity> findByToken(String token);

     List<InterviewLinkEntity> findAll();
}
