package ra.project._11_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project._11_project.model.entity.Appointment;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorAndDateAndStartTimeAndEndTime(
            ra.project._11_project.model.entity.User doctor,
            java.time.LocalDate date,
            java.time.LocalTime startTime,
            java.time.LocalTime endTime
    );
    List<Appointment> findByPatientId(Long patientId);
}