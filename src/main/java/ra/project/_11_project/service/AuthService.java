package ra.project._11_project.service;

import ra.project._11_project.model.dto.request.LoginRequest;
import ra.project._11_project.model.dto.request.RefreshTokenRequest;
import ra.project._11_project.model.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(
            LoginRequest request
    );
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(String token);
}