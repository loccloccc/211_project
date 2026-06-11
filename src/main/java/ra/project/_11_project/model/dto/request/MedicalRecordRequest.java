package ra.project._11_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicalRecordRequest {

    @NotNull(message = "patientId không được để trống")
    private Long patientId;

    @NotBlank(message = "Chuẩn đoán không được để trống")
    private String diagnosis;
}