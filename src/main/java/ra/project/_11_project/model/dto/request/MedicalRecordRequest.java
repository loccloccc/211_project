package ra.project._11_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecordRequest {

    @NotNull(message = "Không được để trống file")
    private MultipartFile file;

    @NotBlank(message = "Không được để trống chẩn đoán")
    private String diagnosis;

    @NotNull(message = "Không được để trống mã bệnh nhân")
    private Long userId;
}