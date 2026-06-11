package ra.project._11_project.service;

import org.springframework.data.domain.Page;
import ra.project._11_project.model.dto.request.ChangePasswordRequest;
import ra.project._11_project.model.dto.request.UserRequest;
import ra.project._11_project.model.dto.response.UserResponse;

public interface UserService {

    UserResponse registerPatient(UserRequest request);

    UserResponse createUser(UserRequest request);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

    UserResponse findById(Long id);

    Page<UserResponse> findAll(String keyword, int page, int size);

    void changePassword(ChangePasswordRequest request);
}