package resume.miles.mandatoryskill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.mandatoryskill.entity.MandatorySkillEntity;

@Repository
public interface MandatorySkillRepository extends JpaRepository<MandatorySkillEntity,Long>{
    List<MandatorySkillEntity> findByJob(Long jobId);
}
