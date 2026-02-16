package resume.miles.doctorTimeSlots.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name="doctor_slot_timings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorTimeSlotsEntity extends BaseEntity{
    @Column(name="slot_time", nullable=false)
        private Integer slot_time;
    @Column(columnDefinition = "integer default 1")
        private Integer status=1;

}
