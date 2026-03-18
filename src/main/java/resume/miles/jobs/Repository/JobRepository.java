package resume.miles.jobs.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.jobs.entity.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity,Long>{
    Optional<JobEntity> findByJobId(String jobId);
    
    long countByStatus(Integer status);
    
    long countByStatusAndCreatedAtAfter(Integer status, java.time.LocalDateTime date);
    @EntityGraph(attributePaths = {"client"})
    List<JobEntity> findAll();

    @EntityGraph(attributePaths = {
        "client",
        "mustHaveSkills",
        "mandatorySkills"
})
List<JobEntity> findAllBy();
    
    List<JobEntity> findTop10ByOrderByCreatedAtDesc();
}
