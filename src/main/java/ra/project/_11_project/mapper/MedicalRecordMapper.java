package ra.project._11_project.mapper;

import org.springframework.stereotype.Component;
import ra.project._11_project.model.dto.response.MedicalRecordResponse;
import ra.project._11_project.model.entity.MedicalRecord;

@Component
public class MedicalRecordMapper {

    public MedicalRecordResponse toResponse(
            MedicalRecord medicalRecord
    ) {

        return MedicalRecordResponse.builder()
                .id(medicalRecord.getId())
                .fileUrl(medicalRecord.getFileUrl())
                .diagnosis(medicalRecord.getDiagnosis())
                .createdAt(medicalRecord.getCreatedAt())
                .patientId(
                        medicalRecord.getUser().getId()
                )
                .doctorId(
                        medicalRecord.getDoctor().getId()
                )
                .build();
    }
}