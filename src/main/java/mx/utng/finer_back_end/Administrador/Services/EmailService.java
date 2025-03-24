package mx.utng.finer_back_end.Administrador.Services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Autowired;


import jakarta.mail.internet.MimeMessage;  


public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoRechazo(String toEmail, String asunto, String mensaje) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();  
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("finner.oficial.2025@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(asunto);
            helper.setText(mensaje, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


