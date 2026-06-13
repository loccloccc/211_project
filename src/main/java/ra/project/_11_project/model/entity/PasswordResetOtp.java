package ra.project._11_project.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Email nhận OTP
    @Column(nullable = false)
    private String email;

    // Mã OTP
    @Column(nullable = false)
    private String otp;

    // Thời gian hết hạn
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    // Đã sử dụng chưa
    @Column(nullable = false)
    private Boolean used;
}