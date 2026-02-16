package resume.miles.users.entity;

import java.time.LocalDate;
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
@Table(name="users")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(name = "f_name", length = 255)
    private String firstName;

    @Column(name = "l_name", length = 255)
    private String lastName;

    @Column(unique = true, length = 255)
    private String username;

    @Column(unique = true, length = 255, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "admin_password_count")
    private Integer adminPasswordCount = 0;

    private String mobile;
    private String avatar;
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private Integer otp;

    @Column(name = "otp_expire")
    private LocalDateTime otpExpire;

    @Column(name = "o_auth")
    private String oAuth;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    private Integer status = 1;

    @Column(name = "is_deleted")
    private Integer isDeleted = 0;

}
