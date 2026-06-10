package ra.project._11_project.service;

import ra.project._11_project.model.dto.request.AppointmentRequest;
import ra.project._11_project.model.dto.response.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    // thêm
    AppointmentResponse createAppointment(
            AppointmentRequest request,
            Long patientId
    );

    // lấy tất cả
    List<AppointmentResponse> getAllAppointments();
}