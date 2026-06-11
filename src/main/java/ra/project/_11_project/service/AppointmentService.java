package ra.project._11_project.service;

import ra.project._11_project.model.dto.request.AppointmentRequest;
import ra.project._11_project.model.dto.response.AppointmentResponse;
import ra.project._11_project.model.entity.StatusEnum;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(
            AppointmentRequest request,
            Long patientId
    );

    List<AppointmentResponse> getAllAppointments();

    List<AppointmentResponse> getMyAppointments(Long patientId);

    AppointmentResponse updateStatus(Long appointmentId, StatusEnum newStatus);
}