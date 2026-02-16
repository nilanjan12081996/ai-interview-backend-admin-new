package resume.miles.sidebar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import resume.miles.sidebar.entity.MasterSidebarEntity;
import resume.miles.sidebar.repository.customeRepo.MasterSidebarCustomRepository;

@Repository
public interface MasterSidebarRepository extends JpaRepository<MasterSidebarEntity,Long>, JpaSpecificationExecutor<MasterSidebarEntity>,MasterSidebarCustomRepository{

   @Query("SELECT m FROM MasterSidebarEntity m WHERE :id IS NULL OR m.id = :id")
   List<MasterSidebarEntity> findByDataOptionalId(@Param("id") Long id);
}
