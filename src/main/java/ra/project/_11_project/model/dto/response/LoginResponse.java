package ra.project._11_project.model.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private Long userId;

    private String username;

    private String email;

    private String role;

    private String accessToken;

    private String refreshToken;
}