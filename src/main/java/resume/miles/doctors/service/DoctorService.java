package resume.miles.doctors.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.doctorTimeSlots.mapper.DoctorTimeSlotMapper;
import resume.miles.doctors.dto.DoctorDTO;
import resume.miles.doctors.entity.DoctorEntity;
import resume.miles.doctors.mapper.DoctorMapper;
import resume.miles.doctors.repository.DoctorRepository;



@Service
public class DoctorService {
    private final DoctorRepository doctorRepo;
     public DoctorService(DoctorRepository doctorRepo){
        this.doctorRepo=doctorRepo;

     }
     @Transactional
     public List<DoctorDTO> getAllDoctors(){
      List  <DoctorEntity> docs=doctorRepo.findAll();
        return docs.stream().map(DoctorMapper::toDto).collect(Collectors.toList());
     }

       @Transactional(readOnly = true)
    public DoctorDTO getDoctorById(Long id) {
        DoctorEntity entity = doctorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + id));

        return DoctorMapper.toDto(entity);
    }

     @Transactional
     public DoctorDTO approveDoctor(Long id){
        DoctorEntity entity=doctorRepo.findById(id)
         .orElseThrow(() -> new RuntimeException("Doctor not found"));
         entity.setAdminStatus(entity.getAdminStatus()!=null&&entity.getAdminStatus()==0?1:0);
         DoctorEntity updateEntity=doctorRepo.save(entity);
          return DoctorMapper.toDto(updateEntity);
     }

     @Transactional
     public DoctorDTO statusChange(Long id){
      DoctorEntity entity=doctorRepo.findById(id)
      .orElseThrow(()->new RuntimeException("Doctor not found"));
      entity.setStatus(entity.getStatus()!=null&& entity.getStatus()==0?1:0);
      DoctorEntity updateStatus=doctorRepo.save(entity);
      return DoctorMapper.toDto(updateStatus);
     }
}
