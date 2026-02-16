package resume.miles.supportcategory.entity;

import resume.miles.config.baseclass.BaseEntity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "supportcategories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportCategoryEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = true)
    private String image;

   
    @Column(name = "parent_id")
    private Long parentId;

    @Column(nullable = false)
    private Integer status = 1; 
}