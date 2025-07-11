package com.ListOnGo.ServerListOnGo.Controllar;

import com.ListOnGo.ServerListOnGo.Model.LoginUserModel;
import com.ListOnGo.ServerListOnGo.Model.UserModel;
import com.ListOnGo.ServerListOnGo.Repository.UserModelRepository;
import com.ListOnGo.ServerListOnGo.Service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/list-on-go")
@CrossOrigin("*")
public class UserController {
    @Autowired
    UserModelRepository userRepo;
    @Autowired
    EmailService emailService;

    @GetMapping("user/hello")
    public String hello(){
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

    @GetMapping("/user/user-name")
    public ResponseEntity<?> getUserName(@RequestParam("id") Long id) {
        String userName = userRepo.findUserNameById(id);
        if (userName == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userName, HttpStatus.OK);
    }

    @GetMapping("/user/isAdmin")
    public ResponseEntity<?> isAdmin(@RequestParam("email") String email){
        try {
            boolean isAdmin= userRepo.checkUserAdminOrNotByEmail(email);
            return new ResponseEntity<>(isAdmin, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.OK);
        }
    }
    @PostMapping("/user/send-otp")
    public ResponseEntity<?>sendOtp(@RequestParam("otp") String otp,@RequestParam("email") String email){
        try {
            emailService.sendOtp(email,otp);
            userRepo.storeOtpWithUsedFalse(otp,email);
            return new ResponseEntity<>("Verify Done",HttpStatus.OK);
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
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
    @GetMapping("/user/check-otp")
    public ResponseEntity<?> checkOtp(@RequestParam("email") String email,@RequestParam("otp") String otp){
        boolean result=userRepo.checkOtpByEmail(email,otp);
        if (result){
            return new ResponseEntity<>(result,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
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


}