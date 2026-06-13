package ra.project._11_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ra.project._11_project.model.dto.request.ChangePasswordRequest;
import ra.project._11_project.model.dto.request.ForgotPasswordRequest;
import ra.project._11_project.model.dto.request.LoginRequest;
import ra.project._11_project.model.dto.request.RefreshTokenRequest;
import ra.project._11_project.model.dto.request.ResetPasswordRequest;
import ra.project._11_project.model.dto.request.UserRequest;
import ra.project._11_project.model.dto.response.ApiDataResponse;
import ra.project._11_project.service.AuthService;
import ra.project._11_project.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    // bệnh nhân đăng ký
    // http://localhost:8080/api/v1/auth/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiDataResponse<?> register(
            @Valid
            @RequestBody
            UserRequest request
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Đăng ký thành công")
                .data(
                        userService.registerPatient(request)
                )
                .build();
    }

    // tất cả đăng nhập
    // http://localhost:8080/api/v1/auth/login
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<?> login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Đăng nhập thành công")
                .data(
                        authService.login(request)
                )
                .build();
    }

    // refresh token
    // http://localhost:8080/api/v1/auth/refresh
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<?> refreshToken(
            @Valid
            @RequestBody
            RefreshTokenRequest request
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Refresh token thành công")
                .data(
                        authService.refreshToken(request)
                )
                .build();
    }

    // tất cả đăng xuất
    // http://localhost:8080/api/v1/auth/logout
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<?> logout(
            @RequestHeader("Authorization")
            String authorizationHeader
    ) {

        String token =
                authorizationHeader.replace(
                        "Bearer ",
                        ""
                );

        authService.logout(token);

        return ApiDataResponse.builder()
                .success(true)
                .message("Đăng xuất thành công")
                .data(null)
                .build();
    }


    // thay đổi mật khẩu khi đã đăng nhập
    // http://localhost:8080/api/v1/auth/change-password
    @PutMapping("/change-password")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public ApiDataResponse<?> changePassword(
            @Valid
            @RequestBody
            ChangePasswordRequest request
    ) {

        userService.changePassword(request);

        return ApiDataResponse.builder()
                .success(true)
                .message("Đổi mật khẩu thành công")
                .build();
    }


    // QUÊN MẬT KHẨU
    // http://localhost:8080/api/v1/auth/forgot-password
    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<?> forgotPassword(
            @Valid
            @RequestBody
            ForgotPasswordRequest request
    ) {

        authService.forgotPassword(request);

        return ApiDataResponse.builder()
                .success(true)
                .message("OTP đã được gửi tới email")
                .build();
    }

    // đặt lại mật khẩu bằng OTP
    // http://localhost:8080/api/v1/auth/reset-password
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<?> resetPassword(
            @Valid
            @RequestBody
            ResetPasswordRequest request
    ) {

        authService.resetPassword(request);

        return ApiDataResponse.builder()
                .success(true)
                .message("Đặt lại mật khẩu thành công")
                .build();
    }
}