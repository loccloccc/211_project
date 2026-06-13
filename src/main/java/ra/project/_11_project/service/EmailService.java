package ra.project._11_project.service;

public interface EmailService {

    void sendOtpEmail(
            String toEmail,
            String otp
    );
}