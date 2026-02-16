package resume.miles.supportcategorycharges.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.supportcategorycharges.entity.SupportCategoryChargesEntity;

@Repository
public interface SupportCategoryChargesRepository extends JpaRepository<SupportCategoryChargesEntity,Long> {
    Optional<SupportCategoryChargesEntity> findBySupportCategory(Long id);
}
