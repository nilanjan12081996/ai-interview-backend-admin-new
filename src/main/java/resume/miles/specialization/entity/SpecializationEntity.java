package resume.miles.specialization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "specializations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    // Mapping LONGTEXT specifically usually ensures Hibernate handles large strings correctly
    @Column(name = "des", columnDefinition = "LONGTEXT")
    private String des;

    @Column(name = "status")
    private Integer status = 1; // Default value matching databaseChangeLog
}