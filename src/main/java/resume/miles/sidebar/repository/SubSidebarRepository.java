package resume.miles.sidebar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import resume.miles.sidebar.entity.MasterSidebarEntity;
import resume.miles.sidebar.entity.SubSidebarEntity;

@Repository
public interface SubSidebarRepository extends JpaRepository<SubSidebarEntity,Long>,JpaSpecificationExecutor<SubSidebarEntity>{

    @Query("SELECT s FROM SubSidebarEntity as s WHERE :id IS NULL OR s.id = :id ")
    List<SubSidebarEntity> findByDataOptionalId(@Param("id") Long id);
}
