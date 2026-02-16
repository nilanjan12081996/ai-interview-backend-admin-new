package resume.miles.doctorTimeSlots.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.doctorTimeSlots.dto.DoctorTimeSlotsDTO;
import resume.miles.doctorTimeSlots.entity.DoctorTimeSlotsEntity;
import resume.miles.doctorTimeSlots.mapper.DoctorTimeSlotMapper;
import resume.miles.doctorTimeSlots.repository.DoctorTimeSlotRepository;
import resume.miles.doctors.dto.DoctorDTO;
import resume.miles.doctors.entity.DoctorEntity;
import resume.miles.doctors.mapper.DoctorMapper;

@Service
public class DoctorTimeSlotService {
    private final DoctorTimeSlotRepository doctorTimeSlotRepository;
    public DoctorTimeSlotService(DoctorTimeSlotRepository doctorTimeSlotRepository)
    {
        this.doctorTimeSlotRepository=doctorTimeSlotRepository;
    }
    @Transactional(readOnly = true)
    public List<DoctorTimeSlotsDTO> allTimeSlot(){
      List<DoctorTimeSlotsEntity> entities=doctorTimeSlotRepository.findAll();
      return entities.stream().map(DoctorTimeSlotMapper::toDto).collect(Collectors.toList());  
    }

     @Transactional(readOnly = true)
    public DoctorTimeSlotsDTO getDoctorSlotById(Long id) {
        DoctorTimeSlotsEntity entity = doctorTimeSlotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Time slot found with id: " + id));

        return DoctorTimeSlotMapper.toDto(entity);
    }
    @Transactional
    public DoctorTimeSlotsDTO updateSlot(Long id,DoctorTimeSlotsDTO dto){
        DoctorTimeSlotsEntity entity=doctorTimeSlotRepository.findById(id)
        .orElseThrow(()->new RuntimeException("Doctor Time Slot not found"));
        if (dto.getSlot_time() != null) {
            entity.setSlot_time(dto.getSlot_time());
        }

        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        DoctorTimeSlotsEntity updatedEntity = doctorTimeSlotRepository.save(entity);
        return DoctorTimeSlotMapper.toDto(updatedEntity);
    }

    @Transactional
    public DoctorTimeSlotsDTO toggleStatus(Long id){
        DoctorTimeSlotsEntity entity=doctorTimeSlotRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Doctor time slot not found"));

        entity.setStatus(entity.getStatus()!=null && entity.getStatus()==1?0:1);
        DoctorTimeSlotsEntity updatedEntity = doctorTimeSlotRepository.save(entity);
        return DoctorTimeSlotMapper.toDto(updatedEntity);


    }

}
