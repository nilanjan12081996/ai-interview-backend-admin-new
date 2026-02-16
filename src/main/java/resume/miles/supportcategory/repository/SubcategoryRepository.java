package resume.miles.supportcategory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import resume.miles.supportcategory.entity.SupportCategoryEntity;

@Repository
public interface SubcategoryRepository extends JpaRepository<SupportCategoryEntity,Long> {

    @Query("SELECT s From SupportCategoryEntity s WHERE (:id IS NULL OR s.id=:id) AND (s.parentId = 0) ")
    List<SupportCategoryEntity> findByIdOrAll(@Param("id") Long id);

    @Query("SELECT s FROM SupportCategoryEntity s WHERE (:id IS NULL OR s.id=:id) AND (s.parentId = :pid)")
    List<SupportCategoryEntity> findByIdOrAllByCategory(@Param("id") Long pid,@Param("id") Long id);

    List<SupportCategoryEntity> findByParentIdNot(Long parentId);
}
