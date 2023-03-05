package com.interview.interviewapi.Controller;

import com.interview.interviewapi.Entity.Enterprises;
import com.interview.interviewapi.Repository.EnterprisesRepo;
import com.interview.interviewapi.Service.AuthService;
import com.interview.interviewapi.Service.EnterprisesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @CrossOrigin(maxAge = 3600)
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
            count = (int) this.repo.countBynameContainingIgnoreCase(search);
        }else{
            count = (int) this.repo.count();
        }
        al.add(0, count);

        //obtener registros
        List<Enterprises> listaEnterprises = this.enterprisesService.getList(page, limit, search);

        map.put("lista", listaEnterprises);
        map.put("conteo", al);

        return ResponseEntity.ok(map);
    }

    @CrossOrigin(maxAge = 3600)
    @GetMapping
    @RequestMapping(value = "readone", method = RequestMethod.GET)
    public ResponseEntity<?> readone(@RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        //obtener registro
        List<Integer> al = new ArrayList<Integer>();
        List<Enterprises> listaEnterprises = this.enterprisesService.getOne(id);
        return ResponseEntity.ok(listaEnterprises);
    }

    @CrossOrigin(maxAge = 3600)
    @PostMapping
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody Enterprises enterprises, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);
        Date date = new Date();

        //Set datos de fecha de creacion y auditoria de usuario
        enterprises.setCreatedDate(date);
        enterprises.setCreatedBy(tokenR);
        enterprises.setStatus(true);

        this.enterprisesService.create(enterprises);

        HashMap<String, String> map = new HashMap<>();
        map.put("status", "ok");
        return ResponseEntity.ok(map);
    }

    @CrossOrigin(maxAge = 3600)
    @PutMapping
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody Enterprises enterprises, @RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        String name = enterprises.getName();
        String phone = enterprises.getPhone();
        String address = enterprises.getAddress();
        Boolean status = enterprises.isStatus();
        Date date = new Date();
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        //set campos a editar y auditoria de usuario
        Enterprises entityToUpdate = this.repo.getOne(id);
        entityToUpdate.setName(name);
        entityToUpdate.setPhone(phone);
        entityToUpdate.setAddress(address);
        entityToUpdate.setStatus(status);
        entityToUpdate.setModifiedDate(date);
        entityToUpdate.setModifiedBy(tokenR);

        this.enterprisesService.update(entityToUpdate);

        HashMap<String, String> map = new HashMap<>();
        map.put("status", "ok");
        return ResponseEntity.ok(map);
    }
}
