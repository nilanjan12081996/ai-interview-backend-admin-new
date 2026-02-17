package resume.miles.jobs.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.jobs.entity.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity,Long>{
    Optional<JobEntity> findByJobId(String jobId);
    @EntityGraph(attributePaths = {"client"})
    List<JobEntity> findAll();

}
