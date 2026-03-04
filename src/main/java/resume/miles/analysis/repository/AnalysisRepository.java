package resume.miles.analysis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.analysis.entity.AnalysisEntity;

@Repository
public interface AnalysisRepository extends JpaRepository<AnalysisEntity,Long>{
    
    Optional<AnalysisEntity>
    findTopByInterviewLinkIdOrderByCreatedAtDesc(Long interviewLinkId);
}
