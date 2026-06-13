package ra.project._11_project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ra.project._11_project.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl
        implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(
            String toEmail,
            String otp
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject(
                "Mã OTP đặt lại mật khẩu"
        );

        message.setText("Xin chào!\n\n"
                        + "Mã OTP của bạn là: "
                        + otp
                        + "\n\nOTP có hiệu lực trong 5 phút."
        );

        mailSender.send(message);
    }
}