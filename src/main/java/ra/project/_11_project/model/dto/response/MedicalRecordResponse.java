package ra.project._11_project.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {

    private Long id;

    private String fileUrl;

    private String diagnosis;

    private LocalDateTime createdAt;

    private Long patientId;

    private Long doctorId;
}