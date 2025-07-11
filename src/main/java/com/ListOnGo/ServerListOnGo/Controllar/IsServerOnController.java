package com.ListOnGo.ServerListOnGo.Controllar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class IsServerOnController {
    @GetMapping("/isServerOn")
    public ResponseEntity<?> health_ok(){
        return new ResponseEntity<>("Server Is On", HttpStatus.OK);
    }
}