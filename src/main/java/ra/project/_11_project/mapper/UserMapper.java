package ra.project._11_project.mapper;

import org.springframework.stereotype.Component;
import ra.project._11_project.model.dto.response.UserResponse;
import ra.project._11_project.model.entity.User;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }
}