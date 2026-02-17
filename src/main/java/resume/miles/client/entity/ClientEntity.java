package resume.miles.client.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import resume.miles.config.baseclass.BaseEntity;

@Entity
@Table(name = "client")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity extends BaseEntity{
    @Column(name = "c_name", nullable = false)
    private String clientName;

    @Column(name = "status", nullable = false)
    private Integer status = 1;
}
