package ra.project._11_project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
    private Boolean isActive;


    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    private List<MedicalRecord> medicalRecords = new ArrayList<>();


    @OneToMany(mappedBy = "user")
    private List<TokenBlacklist> blacklistedTokens = new ArrayList<>();
}
