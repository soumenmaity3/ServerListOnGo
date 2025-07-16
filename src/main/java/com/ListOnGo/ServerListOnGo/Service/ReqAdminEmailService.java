package com.ListOnGo.ServerListOnGo.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ReqAdminEmailService {

    private final JavaMailSender mailSender;

    public ReqAdminEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String email) throws MessagingException {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true);

        helper.setTo(email); // ✅ Send to user
        helper.setSubject("Admin Access Request");

        String link = "https://listongo-req-for-admin.netlify.app/";

        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Admin Access Request</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', sans-serif;\n" +
                "            background-color: #f4f4f4;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "        .container {\n" +
                "            background-color: #ffffff;\n" +
                "            max-width: 600px;\n" +
                "            margin: 40px auto;\n" +
                "            padding: 30px;\n" +
                "            border-radius: 8px;\n" +
                "            box-shadow: 0 4px 8px rgba(0,0,0,0.05);\n" +
                "        }\n" +
                "        .header {\n" +
                "            text-align: center;\n" +
                "            padding-bottom: 20px;\n" +
                "            border-bottom: 1px solid #dddddd;\n" +
                "        }\n" +
                "        .header h2 {\n" +
                "            color: #333333;\n" +
                "        }\n" +
                "        .message {\n" +
                "            margin: 20px 0;\n" +
                "            font-size: 16px;\n" +
                "            color: #444444;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "        .action-button {\n" +
                "            display: inline-block;\n" +
                "            padding: 12px 24px;\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 6px;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            font-size: 13px;\n" +
                "            color: #888888;\n" +
                "            text-align: center;\n" +
                "            margin-top: 30px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <div class=\"header\">\n" +
                "        <h2>Admin Access Request</h2>\n" +
                "    </div>\n" +
                "    <p class=\"message\">\n" +
                "        Hello,<br><br>\n" +
                "        User <strong>" + email + "</strong> has requested admin access.<br>\n" +
                "        Please review and approve this request if appropriate.\n" +
                "    </p>\n" +
                "    <div style=\"text-align: center; margin: 30px 0;\">\n" +
                "        <a class=\"action-button\" href=\"" + link + "\">Approve as Admin</a>\n" +
                "    </div>\n" +
                "    <div class=\"footer\">\n" +
                "        &copy; 2025 ListOnGo. All rights reserved.\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";

        helper.setText(htmlMessage, true);
        helper.setFrom("ListOnGo App For Access Admin <otp.send.by.sm.team@gmail.com>");

        mailSender.send(mailMessage);
    }
}
