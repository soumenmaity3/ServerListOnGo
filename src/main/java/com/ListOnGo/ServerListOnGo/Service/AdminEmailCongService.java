package com.ListOnGo.ServerListOnGo.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AdminEmailCongService {
    private JavaMailSender mailSender;
    private AdminEmailCongService(JavaMailSender mailSender){
        this.mailSender=mailSender;
    }
    public void sendOtp(String to) throws MessagingException {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setTo(to);
        helper.setSubject("Your Admin Access Has Been Approved - ListOnGo");
        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Admin Access Approved</title>\n" +
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
                "        .congrats-box {\n" +
                "            margin: 20px auto;\n" +
                "            text-align: center;\n" +
                "            background-color: #e0ffe0;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            font-size: 20px;\n" +
                "            font-weight: bold;\n" +
                "            color: #2e7d32;\n" +
                "            width: fit-content;\n" +
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
                "        <h2>Welcome to ListOnGo</h2>\n" +
                "    </div>\n" +
                "\n" +
                "    <p class=\"message\">\n" +
                "        Dear User,<br><br>\n" +
                "        Your request has been approved. You now have admin access to the ListOnGo platform.\n" +
                "    </p>\n" +
                "\n" +
                "    <div class=\"congrats-box\">\n" +
                "        🎉 Congratulations! You are now an Admin! 🎉\n" +
                "    </div>\n" +
                "\n" +
                "    <p class=\"message\">\n" +
                "        Please use your new privileges responsibly. You can now access admin features in your dashboard.<br><br>\n" +
                "        Thank you for your contribution to ListOnGo.<br>\n" +
                "        <strong>ListOnGo Team</strong>\n" +
                "    </p>\n" +
                "\n" +
                "    <div class=\"footer\">\n" +
                "        &copy; 2025 ListOnGo. All rights reserved.\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";

        helper.setText(htmlMessage,true);
        helper.setFrom("ListOnGo App For Promotion <otp.send.by.sm.team@gmail.com>");

        mailSender.send(mimeMessage);
    }

    public void sendDenyMail(String to,String reason) throws MessagingException {
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setTo(to);
        helper.setSubject("Your Admin Access Request Has Been Deny - ListOnGo");
        String htmlMessage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Admin Access Denied</title>\n" +
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
                "            color: #b71c1c;\n" +
                "        }\n" +
                "        .message {\n" +
                "            margin: 20px 0;\n" +
                "            font-size: 16px;\n" +
                "            color: #444444;\n" +
                "            line-height: 1.5;\n" +
                "        }\n" +
                "        .deny-box {\n" +
                "            margin: 20px auto;\n" +
                "            text-align: center;\n" +
                "            background-color: #ffebee;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 8px;\n" +
                "            font-size: 18px;\n" +
                "            font-weight: bold;\n" +
                "            color: #c62828;\n" +
                "            width: fit-content;\n" +
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
                "        <h2>Admin Access Denied</h2>\n" +
                "    </div>\n" +
                "\n" +
                "    <p class=\"message\">\n" +
                "        Dear User,<br><br>\n" +
                "        We regret to inform you that your request for admin access on <strong>ListOnGo</strong> has been denied.\n" +
                "    </p>\n" +
                "\n" +
                "    <div class=\"deny-box\">\n" +
                "        ❌ Reason: " + reason + "\n" +
                "    </div>\n" +
                "\n" +
                "    <p class=\"message\">\n" +
                "        If you believe this is a mistake or have questions, please contact our support team.<br><br>\n" +
                "        <strong>ListOnGo Team</strong>\n" +
                "    </p>\n" +
                "\n" +
                "    <div class=\"footer\">\n" +
                "        &copy; 2025 ListOnGo. All rights reserved.\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";

        helper.setText(htmlMessage,true);
        helper.setFrom("ListOnGo App For Deny <otp.send.by.sm.team@gmail.com>");

        mailSender.send(mimeMessage);

    }

}
