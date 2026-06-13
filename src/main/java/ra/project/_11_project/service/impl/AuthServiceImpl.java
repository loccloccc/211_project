package ra.project._11_project.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project._11_project.exception.ForbiddenException;
import ra.project._11_project.exception.UnauthorizedException;
import ra.project._11_project.model.dto.request.ForgotPasswordRequest;
import ra.project._11_project.model.dto.request.LoginRequest;
import ra.project._11_project.model.dto.request.RefreshTokenRequest;
import ra.project._11_project.model.dto.request.ResetPasswordRequest;
import ra.project._11_project.model.dto.response.LoginResponse;
import ra.project._11_project.model.entity.PasswordResetOtp;
import ra.project._11_project.model.entity.TokenBlacklist;
import ra.project._11_project.model.entity.User;
import ra.project._11_project.repository.PasswordResetOtpRepository;
import ra.project._11_project.repository.TokenBlacklistRepository;
import ra.project._11_project.repository.UserRepository;
import ra.project._11_project.security.jwt.JwtProvider;
import ra.project._11_project.service.AuthService;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    // thêm mới
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final JavaMailSender mailSender;

    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        User user = userRepository.findByUsername(
                        request.getUsername()
                )
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Sai tài khoản hoặc mật khẩu"
                        )
                );

        if (!user.getIsActive()) {
            throw new ForbiddenException(
                    "Tài khoản đã bị khóa"
            );
        }

        boolean matched = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        );

        if (!matched) {
            throw new UnauthorizedException(
                    "Sai tài khoản hoặc mật khẩu"
            );
        }

        String accessToken =
                jwtProvider.generateAccessToken(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().name()
                );

        String refreshToken =
                jwtProvider.generateRefreshToken(
                        user.getId(),
                        user.getUsername()
                );

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginResponse refreshToken(
            RefreshTokenRequest request
    ) {

        String refreshToken =
                request.getRefreshToken();

        if (!jwtProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException(
                    "Refresh token không hợp lệ"
            );
        }

        if (tokenBlacklistRepository
                .existsByTokenString(refreshToken)) {

            throw new UnauthorizedException(
                    "Refresh token đã bị thu hồi"
            );
        }

        Claims claims =
                jwtProvider.extractClaims(
                        refreshToken
                );

        Long userId =
                ((Number) claims.get("userId"))
                        .longValue();

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Refresh token không hợp lệ"
                                )
                        );

        String newAccessToken =
                jwtProvider.generateAccessToken(
                        user.getId(),
                        user.getUsername(),
                        user.getRole().name()
                );

        String newRefreshToken =
                jwtProvider.generateRefreshToken(
                        user.getId(),
                        user.getUsername()
                );

        tokenBlacklistRepository.save(
                TokenBlacklist.builder()
                        .tokenString(refreshToken)
                        .revokedAt(LocalDateTime.now())
                        .user(user)
                        .build()
        );

        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Override
    public void logout(
            String token
    ) {

        if (!jwtProvider.validateToken(token)) {
            throw new UnauthorizedException(
                    "Token không hợp lệ"
            );
        }

        if (tokenBlacklistRepository
                .existsByTokenString(token)) {
            return;
        }

        Claims claims =
                jwtProvider.extractClaims(token);

        Long userId =
                ((Number) claims.get("userId"))
                        .longValue();

        User user =
                userRepository.findById(userId)
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Token không hợp lệ"
                                )
                        );

        tokenBlacklistRepository.save(
                TokenBlacklist.builder()
                        .tokenString(token)
                        .revokedAt(LocalDateTime.now())
                        .user(user)
                        .build()
        );
    }

    // =====================================================
    // QUÊN MẬT KHẨU
    // =====================================================

    @Override
    public void forgotPassword(
            ForgotPasswordRequest request
    ) {

        User user = userRepository.findByEmail(
                request.getEmail()
        ).orElse(null);

        // Không báo lỗi để tránh dò email
        if (user == null) {
            return;
        }

        String otp = String.valueOf(
                100000 + new Random().nextInt(900000)
        );

        PasswordResetOtp passwordResetOtp =
                PasswordResetOtp.builder()
                        .email(user.getEmail())
                        .otp(otp)
                        .expiredAt(
                                LocalDateTime.now().plusMinutes(5)
                        )
                        .used(false)
                        .build();

        passwordResetOtpRepository.save(
                passwordResetOtp
        );

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(user.getEmail());
        message.setSubject("Mã OTP đặt lại mật khẩu");

        message.setText(
                "Xin chào " + user.getUsername() +
                        "\n\nMã OTP của bạn là: " + otp +
                        "\nOTP có hiệu lực trong 5 phút."
        );

        mailSender.send(message);
    }

    @Override
    public void resetPassword(
            ResetPasswordRequest request
    ) {

        PasswordResetOtp otpRecord =
                passwordResetOtpRepository
                        .findTopByEmailOrderByIdDesc(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "OTP không tồn tại"
                                )
                        );

        if (otpRecord.getUsed()) {
            throw new UnauthorizedException(
                    "OTP đã được sử dụng"
            );
        }

        if (LocalDateTime.now()
                .isAfter(otpRecord.getExpiredAt())) {

            throw new UnauthorizedException(
                    "OTP đã hết hạn"
            );
        }

        if (!otpRecord.getOtp()
                .equals(request.getOtp())) {

            throw new UnauthorizedException(
                    "OTP không chính xác"
            );
        }

        User user = userRepository.findByEmail(
                        request.getEmail()
                )
                .orElseThrow(() ->
                        new UnauthorizedException(
                                "Email không tồn tại"
                        )
                );

        user.setPasswordHash(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);

        otpRecord.setUsed(true);
        passwordResetOtpRepository.save(
                otpRecord
        );
    }
}