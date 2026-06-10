package ra.project._11_project.mapper;

import org.springframework.stereotype.Component;
import ra.project._11_project.model.dto.response.AppointmentResponse;
import ra.project._11_project.model.entity.Appointment;

@Component
public class AppointmentMapper {

    public AppointmentResponse toResponse(Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .date(appointment.getDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .symptomDescription(
                        appointment.getSymptomDescription()
                )
                .patientId(
                        appointment.getPatient().getId()
                )
                .doctorId(
                        appointment.getDoctor().getId()
                )
                .build();
    }
}