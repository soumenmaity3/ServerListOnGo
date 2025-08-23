package com.ListOnGo.ServerListOnGo.Controllar;

import com.ListOnGo.ServerListOnGo.Model.LoginUserModel;
import com.ListOnGo.ServerListOnGo.Model.RequestedForAdminUserDTO;
import com.ListOnGo.ServerListOnGo.Model.UserModel;
import com.ListOnGo.ServerListOnGo.Repository.UserModelRepository;
import com.ListOnGo.ServerListOnGo.Service.*;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/list-on-go")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserModelRepository userRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    ReqAdminEmailService adminEmail;
    @Autowired
    AdminEmailCongService congService;
    @Autowired
    BuyCreditEmailService buyCredit;
    @Autowired
    UserService userService;

    @GetMapping("user/hello")
    public String hello() {
        return "Hello World";
    }

    @PostMapping("user/signup")
    public ResponseEntity<?> userSignUp(@RequestBody UserModel userModel) {
        try {
            String email = userModel.getEmail();
            String userName = userModel.getUsername();
            String password = userModel.getPassword();

            if (email == null || email.isEmpty()
                    || userName == null || userName.isEmpty()
                    || password == null || password.isEmpty()) {
                return new ResponseEntity<>("Enter all of things", HttpStatus.BAD_REQUEST);
            }

            // Encrypt password here 👇
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encryptedPassword = encoder.encode(password);
            userModel.setPassword(encryptedPassword);

            userRepo.save(userModel);
            return new ResponseEntity<>("Done", HttpStatus.OK);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("uk9qm1cd6qw8199ntw98tap583a")) {
                return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
            } else if (errorMessage.contains("ukkw13ib7i7fwdwm86lq1rbi5gv")) {
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
            }
        }
    }


    @PostMapping("user/login")
    public ResponseEntity<?> userLogin(@RequestBody LoginUserModel loginUserModel) {
        List<String> storeEmail = userRepo.checkUserEmail(loginUserModel.getEmail());

        if (storeEmail == null || storeEmail.isEmpty()) {
            return new ResponseEntity<>("Email Not Found", HttpStatus.NOT_FOUND);
        }

        // Fetch hashed password from DB
        String storedHashedPassword = userRepo.checkUserPasswordByEmail(loginUserModel.getEmail());

        // Compare input password with hashed password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (storedHashedPassword == null || !encoder.matches(loginUserModel.getPassword(), storedHashedPassword)) {
            return new ResponseEntity<>("Password not matched", HttpStatus.BAD_REQUEST);
        }

        Long userId = userRepo.getUserIdByEmail(loginUserModel.getEmail());
        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    @GetMapping("user/get-credit")
    public ResponseEntity<?> usersCredit(@RequestParam("email") String email) {
        try {
            int coin = userRepo.getUserCreditByEmail(email);
            if (coin > 0) {
                return new ResponseEntity<>(coin, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("User Not found", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("user/buy-credit")
    public ResponseEntity<Integer> buyCredit(@RequestParam("email") String email, @RequestParam("credit") Integer credit) {
        try {
            if (credit == null || credit <= 0) {
                return ResponseEntity.badRequest().body(0); // or null if you prefer
            }
            try {
                int coin = userRepo.buyCredit(email, credit);
                if (coin > 0) {
                    buyCredit.sendCreditMessage(email, String.valueOf(credit));
                    return ResponseEntity.ok(credit);
                } else {
                    return ResponseEntity.badRequest().body(0);
                }
            } catch (Exception e) {
                return ResponseEntity.status(500).body(0);
            }
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(0);
        }
    }

    @PostMapping("user/cost-credit")
    public ResponseEntity<?> costCredit(@RequestParam("email") String email, @RequestParam("cost") int cost) throws MessagingException {
        int coin = userRepo.getUserCreditByEmail(email);
        if (coin > 0 && cost > 0) {
            userRepo.costCreditByUserEmail(email, cost);
            return new ResponseEntity<>("Your cost credit is" + cost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("You don't have any credit", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/user-name")
    public ResponseEntity<?> getUserName(@RequestParam("id") Long id) {
        String userName = userRepo.findUserNameById(id);
        if (userName == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping("/user/isAdmin")
    public ResponseEntity<?> isAdmin(@RequestParam("email") String email) {
        try {
            Boolean isAdmin = userRepo.checkUserAdminOrNotByEmail(email);
            int coin = userRepo.getUserCreditByEmail(email);
            if (coin == 0) {
                userService.demotion(email);
            }
            return new ResponseEntity<>(isAdmin, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @PostMapping("/user/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam("otp") String otp, @RequestParam("email") String email) {
        try {
            emailService.sendOtp(email, otp);
            Optional<UserModel> userEmail = userRepo.findUserEmail(email);
            if (userEmail.isPresent()) {

                userRepo.storeOtpWithUsedFalse(otp, email);
                return new ResponseEntity<>("Verify Done", HttpStatus.OK);

            } else {
                ResponseEntity response = new ResponseEntity<>("This email doesn't user", HttpStatus.BAD_REQUEST);
                System.out.println(response);
                return response;
            }
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/user/check-otp")
    public ResponseEntity<?> checkOtp(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        boolean result = userRepo.checkOtpByEmail(email, otp);
        if (result) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/use-otp")
    public ResponseEntity<?> useOtp(@RequestParam("email") String email) {
        int rows = userRepo.afterUseOtpUsedOtpFalse(email);
        if (rows > 0) {
            return ResponseEntity.ok("OTP marked as used");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found or OTP already used");
        }
    }

    @PutMapping("/user/reset-pass")
    public ResponseEntity<?> resetPass(@RequestParam("email") String email,
                                       @RequestParam("password") String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encryptedPassword = encoder.encode(password);

        int result = userRepo.resetPasswordByEmail(email, encryptedPassword);

        if (result > 0) {
            return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Email not found or update failed", HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/user/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestParam("userId") Long userId,
                                           @RequestParam("email") String email,
                                           @RequestParam("password") String password) {
        try {
            Optional<UserModel> userOpt = userRepo.findUserEmail(email);

            if (userOpt.isPresent()) {
                UserModel user = userOpt.get();
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                if (encoder.matches(password, user.getPassword())) {
                    userRepo.detachUserProducts(userId);
                    userRepo.deleteByEmail(email);
                    return new ResponseEntity<>("Account deleted", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("user/send-link")
    public ResponseEntity<?> sendAdminLink(@RequestParam("email") String email) {

        try {
            adminEmail.sendMail(email);
            return new ResponseEntity<>("Done", HttpStatus.OK);
        } catch (MessagingException e) {
            return new ResponseEntity<>("Network Slow", HttpStatus.OK);
        }
    }

    @PutMapping("/user/request-admin")
    public ResponseEntity<?> approveUserAsAdmin(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String reason = payload.getOrDefault("reason", "No reason provided");
        int updated = userRepo.reqUserAdmin(email, reason);
        if (updated > 0) {
            return new ResponseEntity<>("User promoted to admin successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found or already an admin", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/pen-req")
    public ResponseEntity<?> pendingRequest() {
        List<RequestedForAdminUserDTO> pendingList = userRepo.pendingRequest();
        return new ResponseEntity<>(pendingList, HttpStatus.OK);
    }

    @PutMapping("/user/approve-admin")
    public ResponseEntity<?> approveAdmin(@RequestParam("approve") boolean isApprove,
                                          @RequestParam("adEmail") String adEmail,
                                          @RequestParam("userEmail") String userEmail,
                                          @RequestParam(value = "reason", required = false) String reason) {

        if (isApprove) {
            userRepo.makeUserAdmin(adEmail, userEmail, reason);
            try {
                congService.sendOtp(userEmail);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<>("Admin request is Approve", HttpStatus.OK);
        } else {
            try {
                userRepo.denyAdminRequest(adEmail, userEmail, reason);
                congService.sendDenyMail(userEmail, reason);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return new ResponseEntity<>("Request Deny ", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("user/admin-req-status")
    public ResponseEntity<?> requestSuccess(@Param("email") String email) {
        String admin_req_status = userRepo.checkStatus(email);
        return new ResponseEntity<>(admin_req_status, HttpStatus.OK);
    }

    @GetMapping("user/admin-approve-by-/{email}")
    public ResponseEntity<?> approveByThis(@PathVariable("email") String email){
        List<UserModel> approveUser=userRepo.approveByYouUser(email);
        return new ResponseEntity<>(approveUser,HttpStatus.OK);
    }
}