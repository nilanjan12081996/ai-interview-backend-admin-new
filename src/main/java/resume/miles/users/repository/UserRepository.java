package resume.miles.users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import resume.miles.users.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<UserEntity> findByIsDeleted(int isDeleted);
    
}
