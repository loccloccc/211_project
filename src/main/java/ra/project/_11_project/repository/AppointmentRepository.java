package ra.project._11_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project._11_project.model.entity.Appointment;
import ra.project._11_project.model.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorAndDateAndStartTimeAndEndTime(
            User doctor,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );
    List<Appointment> findByPatientId(Long patientId);
}