package resume.miles.doctordays.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name="days")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DaysEntity extends BaseEntity{
    @Column(name="day_name",nullable=false)
    private String day_name;
    @Column(name="short_name",nullable = false)
    private String short_name;
    @Column(name="status",nullable = false)
    private Integer status;
}
