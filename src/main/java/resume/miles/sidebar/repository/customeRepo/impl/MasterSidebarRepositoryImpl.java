package resume.miles.sidebar.repository.customeRepo.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple; // Import Tuple
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import resume.miles.sidebar.dto.MasterSidebarDto;
import resume.miles.sidebar.dto.SubSidebarDto;
import resume.miles.sidebar.entity.MasterSidebarEntity;
import resume.miles.sidebar.entity.SubSidebarEntity;
import resume.miles.sidebar.repository.customeRepo.MasterSidebarCustomRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MasterSidebarRepositoryImpl implements MasterSidebarCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MasterSidebarDto> findCustomSidebarDetails(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
     
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<MasterSidebarEntity> root = query.from(MasterSidebarEntity.class);
        
   
        Join<MasterSidebarEntity, SubSidebarEntity> subJoin = root.join("subSidebarEntity", JoinType.LEFT);

        query.select(cb.tuple(
            root.get("id").alias("mId"),
            root.get("sidebarName").alias("mName"),
            root.get("sidebarShortName").alias("mSidebarShortName"),
            root.get("status").alias("mStatus"),
            root.get("createdAt").alias("mCreatedAt"),
            root.get("updatedAt").alias("mUpdatedAt"),
            
            subJoin.get("id").alias("sId"),
            subJoin.get("subSidebarName").alias("sName"),
            subJoin.get("subSidebarShortName").alias("sShortName"),
            subJoin.get("status").alias("sStatus"),
            subJoin.get("createdAt").alias("sCreatedAt"),
            subJoin.get("updatedAt").alias("sUpdatedAt")
        ));

        
        if (id != null) {
            query.where(cb.equal(root.get("id"), id));
        }
        
        query.orderBy(cb.asc(root.get("id")),cb.asc(subJoin.get("id")));

   
        List<Tuple> results = entityManager.createQuery(query).getResultList();

        Map<Long, MasterSidebarDto> dtoMap = new LinkedHashMap<>();

        for (Tuple t : results) {
            Long masterId = t.get("mId", Long.class);

            
            MasterSidebarDto masterDto = dtoMap.computeIfAbsent(masterId, k -> 
                MasterSidebarDto.builder()
                    .id(masterId)
                    .sidebarName(t.get("mName", String.class))
                    .sidebarShortName(t.get("mSidebarShortName", String.class))
                    .status(t.get("mStatus", Integer.class))
                    .createdAt(t.get("mCreatedAt", java.time.LocalDateTime.class))
                    .updatedAt(t.get("mUpdatedAt", java.time.LocalDateTime.class))
                    .subsidebar(new ArrayList<>()) // Initialize empty list
                    .build()
            );

            // Create Child DTO (if child exists)
            Long subId = t.get("sId", Long.class);
            if (subId != null) {
                SubSidebarDto subDto = SubSidebarDto.builder()
                    .id(subId)
                    .subSidebarName(t.get("sName", String.class))
                    .subSidebarShortName(t.get("sShortName", String.class))
                    .status(t.get("sStatus", Integer.class))
                    .masterSidebarId(masterId)
                    .createdAt(t.get("sCreatedAt", java.time.LocalDateTime.class))
                    .updatedAt(t.get("sUpdatedAt", java.time.LocalDateTime.class))
                    .build();

                // Add child to parent's list
                masterDto.getSubsidebar().add(subDto);
            }
        }

        return new ArrayList<>(dtoMap.values());
    }
}