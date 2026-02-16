package resume.miles.doctors.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import resume.miles.doctors.entity.DoctorEntity;

public interface DoctorRepository extends JpaRepository<DoctorEntity,Long>{
    
    
}
