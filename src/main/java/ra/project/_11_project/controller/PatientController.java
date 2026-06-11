package ra.project._11_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ra.project._11_project.model.dto.request.AppointmentRequest;
import ra.project._11_project.model.dto.response.ApiDataResponse;
import ra.project._11_project.model.dto.response.AppointmentResponse;
import ra.project._11_project.security.principal.CustomUserDetails;
import ra.project._11_project.service.AppointmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients/appointments")
@RequiredArgsConstructor
public class PatientController {

    private final AppointmentService appointmentService;

    // đặt lịch
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiDataResponse<?> createAppointment(
            @RequestBody AppointmentRequest request,
            @RequestParam Long patientId
    ) {
        return ApiDataResponse.builder()
                .success(true)
                .message("Đặt lịch thành công")
                .data(appointmentService.createAppointment(request, patientId))
                .build();
    }



    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AppointmentResponse> getMyAppointments() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        Long patientId = user.getUserId();

        return appointmentService.getMyAppointments(patientId);
    }
}