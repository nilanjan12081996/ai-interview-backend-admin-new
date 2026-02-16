package resume.miles.doctordays.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import resume.miles.doctordays.dto.DaysDTO;
import resume.miles.doctordays.entity.DaysEntity;
import resume.miles.doctordays.mapper.DaysMapper;
import resume.miles.doctordays.respository.DaysRepository;
import resume.miles.doctors.entity.DoctorEntity;
import resume.miles.doctors.mapper.DoctorMapper;


@Service
public class DaysService {
    private final DaysRepository daysRepo;
    public DaysService(DaysRepository daysRepo){
        this.daysRepo=daysRepo;
    }

    @Transactional
    public List<DaysDTO> getAllDays(){
        List <DaysEntity> day=daysRepo.findAll();
         return day.stream().map(DaysMapper::toDto).collect(Collectors.toList());
    }
    @Transactional
    public DaysDTO toggleStatus(Long id){
        DaysEntity entity =daysRepo.findById(id)
        .orElseThrow(()->new RuntimeException("Days not found"));
        entity.setStatus(entity.getStatus()!=null&&entity.getStatus()==0?1:0);
        DaysEntity updateStatus=daysRepo.save(entity);
        return DaysMapper.toDto(updateStatus);
       
    }
}
