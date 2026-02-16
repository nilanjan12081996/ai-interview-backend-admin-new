package resume.miles.supportcategorycharges.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name="support-platform-charges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportCategoryChargesEntity extends BaseEntity{
    @Column(name="suport_category", nullable = false)
    private Long supportCategory;
    @Column(name="charge",nullable = false)
    private Integer charge;
    @Column(name = "status",nullable = false)
    private Integer status;
}
