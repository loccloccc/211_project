package ra.project._11_project.model.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiDataResponse<T> {

    private boolean success;

    private String message;

    private T data;
}