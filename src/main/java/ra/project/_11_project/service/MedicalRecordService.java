package ra.project._11_project.service;

import org.springframework.web.multipart.MultipartFile;
import ra.project._11_project.model.dto.response.MedicalRecordResponse;

public interface MedicalRecordService {

    MedicalRecordResponse uploadMedicalRecord(
            Long patientId,
            String diagnosis,
            MultipartFile file
    );
}