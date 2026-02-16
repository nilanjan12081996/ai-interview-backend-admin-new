package resume.miles.awarness.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import resume.miles.awarness.entiry.AwarenessEntity;


@Repository
public interface AwarnessRepository extends JpaRepository<AwarenessEntity,Long>,JpaSpecificationExecutor<AwarenessEntity> {
    List<AwarenessEntity> findBySubsidebarId(Long id);

    Long countById(Long id);
}
