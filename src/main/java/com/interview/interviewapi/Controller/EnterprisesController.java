package com.interview.interviewapi.Controller;

import com.interview.interviewapi.Entity.Enterprises;
import com.interview.interviewapi.Repository.EnterprisesRepo;
import com.interview.interviewapi.Service.AuthService;
import com.interview.interviewapi.Service.EnterprisesService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("enterprises")
public class EnterprisesController {

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EnterprisesRepo repo;

    @GetMapping
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("search") String search, @RequestHeader("Authorization") String bearerToken){
        String tokenR = authService.validateJWT(bearerToken);

        HashMap<String, List> map = new HashMap<>();
        List<Integer> al = new ArrayList<Integer>();
        int count  = 0;
        if(search.length() > 2){
            count = (int) this.repo.countBynameContainingIgnoreCase(search);
        }else{
            count = (int) this.repo.count();
        }
        al.add(0, count);

        List<Enterprises> listaEnterprises = this.enterprisesService.getList(page, limit, search);

        map.put("lista", listaEnterprises);
        map.put("conteo", al);

        return ResponseEntity.ok(map);
    }

    @GetMapping
    @RequestMapping(value = "readone", method = RequestMethod.GET)
    public ResponseEntity<?> readone(@RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        String tokenR = authService.validateJWT(bearerToken);

        List<Integer> al = new ArrayList<Integer>();
        List<Enterprises> listaEnterprises = this.enterprisesService.getOne(id);
        return ResponseEntity.ok(listaEnterprises);
    }

    @PostMapping
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody Enterprises enterprises, @RequestHeader("Authorization") String bearerToken){
        Date date = new Date();
        String tokenR = authService.validateJWT(bearerToken);

        enterprises.setCreatedDate(date);
        enterprises.setCreatedBy(tokenR);
        enterprises.setStatus(true);

        this.enterprisesService.create(enterprises);
        return ResponseEntity.ok("ok");
    }

    @PutMapping
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody Enterprises enterprises, @RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        String name = enterprises.getName();
        String phone = enterprises.getPhone();
        String address = enterprises.getAddress();
        Boolean status = enterprises.isStatus();
        Date date = new Date();
        String tokenR = authService.validateJWT(bearerToken);

        Enterprises entityToUpdate = this.repo.getOne(id);
        entityToUpdate.setName(name);
        entityToUpdate.setPhone(phone);
        entityToUpdate.setAddress(address);
        entityToUpdate.setStatus(status);
        entityToUpdate.setModifiedDate(date);
        entityToUpdate.setModifiedBy(tokenR);

        this.enterprisesService.update(entityToUpdate);

        return ResponseEntity.ok("ok");
    }
}
