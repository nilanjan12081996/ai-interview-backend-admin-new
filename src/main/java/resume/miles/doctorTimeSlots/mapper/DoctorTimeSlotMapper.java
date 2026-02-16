package resume.miles.doctorTimeSlots.mapper;

import resume.miles.doctorTimeSlots.dto.DoctorTimeSlotsDTO;
import resume.miles.doctorTimeSlots.entity.DoctorTimeSlotsEntity;

public class DoctorTimeSlotMapper {
   
    //Entity to DTO
    public static DoctorTimeSlotsDTO toDto(DoctorTimeSlotsEntity doctorTimeSlotsEntity)

    {
        if(doctorTimeSlotsEntity==null)
        {
            return null;
        }
        return DoctorTimeSlotsDTO.builder()
        .id(doctorTimeSlotsEntity.getId())
        .slot_time(doctorTimeSlotsEntity.getSlot_time())
        .status(doctorTimeSlotsEntity.getStatus())
        .createdAt(doctorTimeSlotsEntity.getCreatedAt())
        .updatedAt(doctorTimeSlotsEntity.getUpdatedAt())
        .build();

    }
    //DTO to Entity
    public static DoctorTimeSlotsEntity toEntity(DoctorTimeSlotsDTO dto) {
        if (dto == null) {
            return null;
        }

        DoctorTimeSlotsEntity entity = new DoctorTimeSlotsEntity();
        entity.setSlot_time(dto.getSlot_time());
        entity.setStatus(dto.getStatus()); // no default logic here

        return entity;
    }
}
