package resume.miles.musthaveskill.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.musthaveskill.entity.MustHaveSkillEntity;

@Repository
public interface MustHaveSkillRepository extends JpaRepository<MustHaveSkillEntity,Long> {
    
}
