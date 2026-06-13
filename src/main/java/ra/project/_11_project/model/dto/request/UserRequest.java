package ra.project._11_project.model.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.project._11_project.model.entity.RoleEnum;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequest {

    @NotBlank(message = "Username không được để trống")
    private String username;

    @Email(message = "Email không đúng định dạng")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @Size(
            min = 6,
            message = "Độ dài mật khẩu phải từ 6 ký tự"
    )
    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    private Boolean isActive = true;
}