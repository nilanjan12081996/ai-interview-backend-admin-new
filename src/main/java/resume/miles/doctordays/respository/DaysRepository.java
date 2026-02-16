package resume.miles.doctordays.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import resume.miles.doctordays.entity.DaysEntity;

public interface DaysRepository extends JpaRepository<DaysEntity,Long>{

}
