package ra.project._11_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ra.project._11_project.model.dto.request.UserRequest;
import ra.project._11_project.model.dto.response.ApiDataResponse;
import ra.project._11_project.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiDataResponse<?> register(
            @Valid @RequestBody UserRequest request
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Đăng ký thành công")
                .data(
                        userService.registerPatient(request)
                )
                .build();
    }
}