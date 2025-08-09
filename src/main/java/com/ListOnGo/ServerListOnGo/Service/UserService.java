package com.ListOnGo.ServerListOnGo.Service;

import com.ListOnGo.ServerListOnGo.Repository.UserModelRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserModelRepository userRepo;

    public void demotion(String email) throws MessagingException {
        userRepo.demotionAdmin(email);
    }
}