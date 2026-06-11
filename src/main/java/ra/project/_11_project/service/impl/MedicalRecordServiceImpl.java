package ra.project._11_project.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.project._11_project.exception.ResourceNotFoundException;
import ra.project._11_project.mapper.MedicalRecordMapper;
import ra.project._11_project.model.dto.response.MedicalRecordResponse;
import ra.project._11_project.model.entity.MedicalRecord;
import ra.project._11_project.model.entity.User;
import ra.project._11_project.repository.MedicalRecordRepository;
import ra.project._11_project.repository.UserRepository;
import ra.project._11_project.service.MedicalRecordService;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl
        implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final UserRepository userRepository;
    private final MedicalRecordMapper medicalRecordMapper;
    private final Cloudinary cloudinary;

    @Override
    public MedicalRecordResponse uploadMedicalRecord(
            Long patientId,
            String diagnosis,
            MultipartFile file
    ) {

        try {
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User doctor =
                    userRepository.findByUsername(username)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Không tìm thấy bác sĩ"
                                    ));

            User patient =
                    userRepository.findById(patientId)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Không tìm thấy bệnh nhân"
                                    ));

            Map uploadResult =
                    cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.emptyMap()
                    );

            String fileUrl =
                    uploadResult.get("secure_url")
                            .toString();

            MedicalRecord medicalRecord =
                    MedicalRecord.builder()
                            .fileUrl(fileUrl)
                            .diagnosis(diagnosis)
                            .createdAt(LocalDateTime.now())
                            .user(patient)
                            .doctor(doctor)
                            .build();

            medicalRecord = medicalRecordRepository.save(medicalRecord);

            return medicalRecordMapper.toResponse(medicalRecord);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Upload hồ sơ bệnh án thất bại"
            );
        }
    }
}