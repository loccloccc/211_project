package ra.project._11_project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ra.project._11_project.model.dto.request.UserRequest;
import ra.project._11_project.model.dto.response.ApiDataResponse;
import ra.project._11_project.service.UserService;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiDataResponse<?> getAll(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Danh sách người dùng")
                .data(
                        userService.findAll(
                                keyword,
                                page,
                                size
                        )
                )
                .build();
    }

    @GetMapping("/{id}")
    public ApiDataResponse<?> findById(
            @PathVariable Long id
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Chi tiết người dùng")
                .data(
                        userService.findById(id)
                )
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiDataResponse<?> create(
            @Valid @RequestBody UserRequest request
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Tạo thành công")
                .data(
                        userService.createUser(request)
                )
                .build();
    }

    @PutMapping("/{id}")
    public ApiDataResponse<?> update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Cập nhật thành công")
                .data(
                        userService.updateUser(id, request)
                )
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiDataResponse<?> delete(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ApiDataResponse.builder()
                .success(true)
                .message("Xóa thành công")
                .data(null)
                .build();
    }
}