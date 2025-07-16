package com.ListOnGo.ServerListOnGo.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class DemotionEmailService {
    private final JavaMailSender mailSender;

    public DemotionEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to) throws MessagingException {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setTo(to);
        helper.setSubject("You are no longer an Admin (Account Demotion)");
        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Admin Access Revoked</title>\n" +
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
                "            color: #cc0000;\n" +
                "        }\n" +
                "        .message {\n" +
                "            margin: 20px 0;\n" +
                "            font-size: 16px;\n" +
                "            color: #444444;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "        .alert-box {\n" +
                "            margin: 20px auto;\n" +
                "            text-align: center;\n" +
                "            background-color: #ffe0e0;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            color: #b71c1c;\n" +
                "            width: fit-content;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            margin: 20px auto;\n" +
                "            background-color: #007BFF;\n" +
                "            color: white;\n" +
                "            padding: 12px 24px;\n" +
                "            text-align: center;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 5px;\n" +
                "            font-size: 16px;\n" +
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
                "        <h2>ListOnGo Admin Update</h2>\n" +
                "    </div>\n" +
                "\n" +
                "    <p class=\"message\">\n" +
                "        Dear User,<br><br>\n" +
                "        We would like to inform you that your admin access on the ListOnGo platform has been revoked.\n" +
                "    </p>\n" +
                "\n" +
                "    <div class=\"alert-box\">\n" +
                "        ⚠️ You are no longer an Admin ⚠️\n" +
                "    </div>\n" +
                "\n" +
                "    <p class=\"message\">\n" +
                "        This decision may have been made due to inactivity, policy changes, or other administrative reasons.\n" +
                "        If this change affects your ongoing usage, you can restore your privileges.\n" +
                "    </p>\n" +
                "\n" +
                "    <p class=\"message\" style=\"text-align: center; font-weight: bold;\">\n" +
                "        Buy credit to continue using admin features:\n" +
                "    </p>\n" +
                "\n" +
                "    <div style=\"text-align: center;\">\n" +
                "        <a href=\"https://yourdomain.com/buy-credit\" class=\"button\">Buy Credit</a>\n" +//credit link
                "    </div>\n" +
                "\n" +
                "    <p class=\"message\">\n" +
                "        If you believe this was an error or you have any questions, please contact our support team.<br><br>\n" +
                "        <strong>— ListOnGo Team</strong>\n" +
                "    </p>\n" +
                "\n" +
                "    <div class=\"footer\">\n" +
                "        &copy; 2025 ListOnGo. All rights reserved.\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";

        helper.setText(htmlMessage,true);
        helper.setFrom("ListOnGo App For Demotion <otp.send.by.sm.team@gmail.com>");

        mailSender.send(mimeMessage);
    }
}
