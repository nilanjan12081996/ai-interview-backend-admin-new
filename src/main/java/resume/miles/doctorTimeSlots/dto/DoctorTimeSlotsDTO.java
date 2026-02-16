package resume.miles.doctorTimeSlots.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorTimeSlotsDTO {
    private Long id;
    @NotNull(message="Time Slot is required")
    private Integer slot_time;
     private Integer status;
     private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
