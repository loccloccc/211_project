package ra.project._11_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.project._11_project.exception.ConflictException;
import ra.project._11_project.exception.ResourceNotFoundException;
import ra.project._11_project.mapper.AppointmentMapper;
import ra.project._11_project.model.dto.request.AppointmentRequest;
import ra.project._11_project.model.dto.response.AppointmentResponse;
import ra.project._11_project.model.entity.Appointment;
import ra.project._11_project.model.entity.StatusEnum;
import ra.project._11_project.model.entity.User;
import ra.project._11_project.repository.AppointmentRepository;
import ra.project._11_project.repository.UserRepository;
import ra.project._11_project.service.AppointmentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentResponse createAppointment(
            AppointmentRequest request,
            Long patientId
    ) {

        User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy bác sĩ"));

        User patient = userRepository.findById(patientId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy bệnh nhân"));

        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new ConflictException(
                    "Thời gian kết thúc phải sau thời gian bắt đầu"
            );
        }

        boolean exists = appointmentRepository.existsByDoctorAndDateAndStartTimeAndEndTime(
                        doctor,
                        request.getDate(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        if (exists) {
            throw new ConflictException("Bác sĩ đã có lịch khám trong khung giờ này");
        }

        Appointment appointment = Appointment.builder()
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(StatusEnum.PENDING)
                .symptomDescription(request.getSymptomDescription())
                .doctor(doctor)
                .patient(patient)
                .build();

        return appointmentMapper.toResponse(
                appointmentRepository.save(appointment)
        );
    }

    @Override
    public List<AppointmentResponse> getAllAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public List<AppointmentResponse> getMyAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    @Override
    public AppointmentResponse updateStatus(
            Long appointmentId,
            StatusEnum newStatus
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentDoctor = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy bác sĩ đang đăng nhập"
                        ));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Không tìm thấy lịch khám"
                        ));


        // Chỉ bác sĩ sở hữu lịch khám mới được cập nhật
        if (!appointment.getDoctor().getId()
                .equals(currentDoctor.getId())) {

            throw new ConflictException(
                    "Bạn không có quyền cập nhật lịch khám này"
            );
        }

        StatusEnum current = appointment.getStatus();

        if (current == StatusEnum.COMPLETED || current == StatusEnum.CANCELLED) {
            throw new ConflictException("Lịch đã kết thúc, không thể thay đổi");
        }

        switch (current) {

            case PENDING -> {
                if (newStatus != StatusEnum.CONFIRMED
                        && newStatus != StatusEnum.CANCELLED) {

                    throw new ConflictException(
                            "PENDING chỉ được CONFIRMED hoặc CANCELLED"
                    );
                }
            }

            case CONFIRMED -> {
                if (newStatus != StatusEnum.COMPLETED
                        && newStatus != StatusEnum.CANCELLED) {

                    throw new ConflictException(
                            "CONFIRMED chỉ được COMPLETED hoặc CANCELLED"
                    );
                }
            }
        }

        appointment.setStatus(newStatus);

        appointment = appointmentRepository.save(appointment);

        return appointmentMapper.toResponse(appointment);
    }
}