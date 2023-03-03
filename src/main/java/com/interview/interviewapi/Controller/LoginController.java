package com.interview.interviewapi.Controller;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import com.interview.interviewapi.Repository.UserRepo;
import com.interview.interviewapi.Service.AuthService;
import com.interview.interviewapi.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserRepo repo;

    @Autowired
    private AuthService authService;

    @GetMapping
    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public ResponseEntity<?> auth(@RequestParam("username") String username, @RequestParam("password") String password) throws NoSuchAlgorithmException {
        HashMap<String, String> map = new HashMap<>();

        int userCount = this.repo.countByusernameContainingIgnoreCase(username);

        if(userCount < 1){
            map.put("status", "error");
            map.put("code", "01");
            map.put("message", "Nombre de usuario no existe en el sistema.");
            return ResponseEntity.ok(map);
        }

        User userOnDb = this.repo.getUserByusernameContainingIgnoreCase(username);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String passFromRequest = DatatypeConverter.printHexBinary(digest);
        if(userOnDb.getPassword().equals(passFromRequest)){
            map.put("status", "ok");
            map.put("token", authService.getJWTToken(username));
            map.put("message", "Autenticación correcta.");
            return ResponseEntity.ok(map);
        }else{
            map.put("status", "error");
            map.put("code", "02");
            map.put("message", "Contraseña incorrecta.");
            return ResponseEntity.ok(map);
        }
    }
}
