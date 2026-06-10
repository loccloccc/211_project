package ra.project._11_project.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ra.project._11_project.model.dto.request.AppointmentRequest;
import ra.project._11_project.model.dto.response.ApiDataResponse;
import ra.project._11_project.model.dto.response.AppointmentResponse;
import ra.project._11_project.service.AppointmentService;

@RestController
@RequestMapping("/api/v1/patient/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiDataResponse<?> createAppointment(
            @Valid @RequestBody AppointmentRequest request,
            @RequestParam Long patientId
    ) {

        return ApiDataResponse.builder()
                .success(true)
                .message("Đặt lịch thành công")
                .data(
                        appointmentService  .createAppointment(
                                request,
                                patientId
                        )
                )
                .build();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiDataResponse<?> getAllAppointments() {

        return ApiDataResponse.builder()
                .success(true)
                .message("Danh sách lịch khám")
                .data(
                        appointmentService.getAllAppointments()
                )
                .build();
    }
}