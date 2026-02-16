package resume.miles.doctors.entity;

import java.time.LocalDateTime;

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
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorEntity extends BaseEntity{
  @Column(name = "f_name", nullable = false)
    private String firstName;

    @Column(name = "l_name", nullable = false)
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(unique = true, nullable = false, length = 20)
    private String mobile;

    private String avatar;

    private Integer otp;

    @Column(name = "otp_expire")
    private LocalDateTime otpExpire;

    @Column(name = "o_auth")
    private String oAuth;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(nullable = false)
    private Integer status;

    @Column(name = "admin_status", nullable = false)
    private Integer adminStatus;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;
}
