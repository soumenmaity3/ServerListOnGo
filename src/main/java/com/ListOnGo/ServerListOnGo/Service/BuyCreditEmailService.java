package com.ListOnGo.ServerListOnGo.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class BuyCreditEmailService {
   private final JavaMailSender mailSender;

    public BuyCreditEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void sendCreditMessage(String to, String credit) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject("Your Credit Balance Update");

        String htmlMessage = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Credit Notification</title>" +
                "<style>" +
                "body { font-family: 'Segoe UI', sans-serif; background-color: #f4f4f4; margin: 0; padding: 0; }" +
                ".container { background-color: #ffffff; max-width: 600px; margin: 40px auto; padding: 30px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.05); }" +
                ".header { text-align: center; padding-bottom: 20px; border-bottom: 1px solid #dddddd; }" +
                ".header h2 { color: #333333; }" +
                ".credit-box { margin: 20px auto; text-align: center; background-color: #e6f7e6; padding: 16px; border-radius: 6px; font-size: 24px; font-weight: bold; color: #2e7d32; width: fit-content; }" +
                ".message { margin: 20px 0; font-size: 16px; color: #444444; line-height: 1.5; }" +
                ".footer { font-size: 13px; color: #888888; text-align: center; margin-top: 30px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'><h2>Credit Update</h2></div>" +
                "<p class='message'>" +
                "Dear User,<br><br>" +
                "We are pleased to inform you that your current credit balance has been updated. Below is your available credit:" +
                "</p>" +
                "<div class='credit-box'>" + credit + "</div>" +
                "<p class='message'>" +
                "If you have any questions or concerns, feel free to reach out to our support team.<br><br>" +
                "Thank you,<br><strong>ListOnGo Team</strong>" +
                "</p>" +
                "<div class='footer'>&copy; 2025 ListOnGo. All rights reserved.</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        helper.setText(htmlMessage, true);
        helper.setFrom("ListOnGo App <otp.send.by.sm.team@gmail.com>");
        mailSender.send(mimeMessage);
    }

}
