package ra.project._11_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.project._11_project.model.dto.response.ApiDataResponse;
import ra.project._11_project.model.dto.response.AppointmentResponse;
import ra.project._11_project.model.dto.response.MedicalRecordResponse;
import ra.project._11_project.model.entity.StatusEnum;
import ra.project._11_project.service.AppointmentService;
import ra.project._11_project.service.MedicalRecordService;

@RestController
@RequestMapping("/api/v1/doctors/appointments")
@RequiredArgsConstructor
public class DoctorController {

    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('DOCTOR')")
    public ApiDataResponse<?> updateStatus(
            @PathVariable Long id,
            @RequestParam StatusEnum status
    ) {

        AppointmentResponse response = appointmentService.updateStatus(id, status);

        return ApiDataResponse.builder()
                .success(true)
                .message("Cập nhật trạng thái thành công")
                .data(response)
                .build();
    }

    @PostMapping("/records/upload")
    @PreAuthorize("hasRole('DOCTOR')")
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<?> uploadMedicalRecord(

            @RequestParam Long patientId,

            @RequestParam String diagnosis,

            @RequestParam MultipartFile file
    ) {

        MedicalRecordResponse response =
                medicalRecordService
                        .uploadMedicalRecord(
                                patientId,
                                diagnosis,
                                file
                        );

        return ApiDataResponse.builder()
                .success(true)
                .message(
                        "Tải hồ sơ bệnh án thành công"
                )
                .data(response)
                .build();
    }
}