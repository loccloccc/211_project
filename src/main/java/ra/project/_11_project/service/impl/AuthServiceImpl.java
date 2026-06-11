package ra.project._11_project.service.impl;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project._11_project.exception.ConflictException;
import ra.project._11_project.exception.ResourceNotFoundException;
import ra.project._11_project.model.dto.request.LoginRequest;
import ra.project._11_project.model.dto.request.RefreshTokenRequest;
import ra.project._11_project.model.dto.response.LoginResponse;
import ra.project._11_project.model.entity.TokenBlacklist;
import ra.project._11_project.model.entity.User;
import ra.project._11_project.repository.TokenBlacklistRepository;
import ra.project._11_project.repository.UserRepository;
import ra.project._11_project.security.jwt.JwtProvider;
import ra.project._11_project.service.AuthService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        User user = userRepository.findByUsername(
                        request.getUsername()
                )
                .orElseThrow(
                        () -> new ConflictException(
                                "Sai tài khoản hoặc mật khẩu"
                        )
                );

        if (!user.getIsActive()) {
            throw new ConflictException(
                    "Tài khoản đã bị khóa"
            );
        }

        boolean matched =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPasswordHash()
                );

        if (!matched) {
            throw new ConflictException(
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
            throw new ConflictException(
                    "Refresh token không hợp lệ"
            );
        }

        if (
                tokenBlacklistRepository
                        .existsByTokenString(refreshToken)
        ) {
            throw new ConflictException(
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
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Không tìm thấy user"
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
            throw new ConflictException(
                    "Token không hợp lệ"
            );
        }

        Claims claims =
                jwtProvider.extractClaims(token);

        Long userId =
                ((Number) claims.get("userId"))
                        .longValue();

        User user =
                userRepository.findById(userId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Không tìm thấy user"
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
}