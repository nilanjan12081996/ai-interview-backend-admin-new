package resume.miles.doctorTimeSlots.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import resume.miles.doctorTimeSlots.entity.DoctorTimeSlotsEntity;

public interface DoctorTimeSlotRepository extends JpaRepository<DoctorTimeSlotsEntity,Long>{

}
