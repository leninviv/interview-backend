package com.interview.interviewapi.Controller;

import com.interview.interviewapi.Entity.User;
import com.interview.interviewapi.Repository.UserRepo;
import com.interview.interviewapi.Service.AuthService;
import com.interview.interviewapi.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepo repo;

    @GetMapping
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("search") String search, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        HashMap<String, List> map = new HashMap<>();
        List<Integer> al = new ArrayList<Integer>();

        //conteo de registros, con busqueda o sin busqueda
        int count  = 0;
        if(search.length() > 2){
            count = (int) this.repo.countByusernameContainingIgnoreCase(search);
        }else{
            count = (int) this.repo.count();
        }
        al.add(0, count);

        //obtener registros
        List<User> listaEnterprises = this.userService.getList(page, limit, search);

        map.put("lista", listaEnterprises);
        map.put("conteo", al);

        return ResponseEntity.ok(map);
    }

    @GetMapping
    @RequestMapping(value = "readone", method = RequestMethod.GET)
    public ResponseEntity<?> readone(@RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        //obtener registro
        List<Integer> al = new ArrayList<Integer>();
        List<User> listaUsers = this.userService.getOne(id);
        return ResponseEntity.ok(listaUsers);
    }

    @PostMapping
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody User user, @RequestHeader("Authorization") String bearerToken) throws NoSuchAlgorithmException {
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);
        Date date = new Date();
        HashMap<String, String> map = new HashMap<>();

        //validacion de username (no repetido)
        int userCount = this.repo.countByusernameContainingIgnoreCase(user.getUsername());
        if(userCount > 0){
            map.put("status", "error");
            map.put("code", "03");
            map.put("message", "Nombre de usuario ya ha sido registrado.");
            return ResponseEntity.ok(map);
        }

        //hash a md5 la contraseña
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(user.getPassword().getBytes(StandardCharsets.UTF_8));
        String passFromRequest = DatatypeConverter.printHexBinary(digest);

        //Set datos de fecha de creacion y auditoria de usuario
        user.setPassword(passFromRequest);
        user.setCreatedDate(date);
        user.setCreatedBy(tokenR);
        user.setStatus(true);

        this.userService.create(user);
        return ResponseEntity.ok("ok");
    }

    @PutMapping
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody User user, @RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken) throws NoSuchAlgorithmException {
        String username = user.getUsername();
        String password = user.getPassword();
        int rol = user.getRol();
        Boolean status = user.isStatus();
        HashMap<String, String> map = new HashMap<>();

        Date date = new Date();
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        //validacion de username (no repetido) Omitiendo el usuario con id que se edita
        long userCount = this.repo.countByusernameEdit(user.getUsername(), id);
        if(userCount > 0){
            map.put("status", "error");
            map.put("code", "03");
            map.put("message", "Nombre de usuario ya ha sido registrado.");
            return ResponseEntity.ok(map);
        }

        User entityToUpdate = this.repo.getOne(id);
        entityToUpdate.setUsername(username);

        //si la contraseña no es vacio, se edita
        if(password.length() > 0){
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            String passFromRequest = DatatypeConverter.printHexBinary(digest);
            user.setPassword(passFromRequest);
            entityToUpdate.setPassword(passFromRequest);
        }

        //set campos a editar y auditoria de usuario
        entityToUpdate.setRol(rol);
        entityToUpdate.setStatus(status);
        entityToUpdate.setModifiedDate(date);
        entityToUpdate.setModifiedBy(tokenR);

        this.userService.update(entityToUpdate);

        return ResponseEntity.ok("ok");
    }
}
