package resume.miles.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.client.entity.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity,Long>{
     List<ClientEntity> findByStatus(Integer status);
     Optional<ClientEntity> findByClientName(String clientName);
    
}
