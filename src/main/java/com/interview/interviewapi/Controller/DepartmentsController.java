package com.interview.interviewapi.Controller;

import com.interview.interviewapi.Entity.Departments;
import com.interview.interviewapi.Repository.DepartmentsRepo;
import com.interview.interviewapi.Service.AuthService;
import com.interview.interviewapi.Service.DepartmentsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("departments")
public class DepartmentsController {
    @Autowired
    private DepartmentsService departmentsService;

    @Autowired
    private AuthService authService;

    @Autowired
    private DepartmentsRepo repo;

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
        List<Departments> listaDepartments = this.departmentsService.getList(page, limit, search);

        map.put("lista", listaDepartments);
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
        List<Departments> listaDepartments = this.departmentsService.getOne(id);
        return ResponseEntity.ok(listaDepartments);
    }

    @PostMapping
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody Departments departments, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);
        Date date = new Date();

        //Set datos de fecha de creacion y auditoria de usuario
        departments.setCreatedDate(date);
        departments.setCreatedBy(tokenR);
        departments.setStatus(true);

        this.departmentsService.create(departments);
        return ResponseEntity.ok("ok");
    }

    @PutMapping
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody Departments departments, @RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        int idEnterprise = departments.getIdEnterprise();
        String name = departments.getName();
        String description = departments.getDescription();
        String phone = departments.getPhone();
        Boolean status = departments.isStatus();
        Date date = new Date();
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        //set campos a editar y auditoria de usuario
        Departments entityToUpdate = this.repo.getOne(id);
        entityToUpdate.setIdEnterprise(idEnterprise);
        entityToUpdate.setName(name);
        entityToUpdate.setDescription(description);
        entityToUpdate.setPhone(phone);
        entityToUpdate.setStatus(status);
        entityToUpdate.setModifiedDate(date);
        entityToUpdate.setModifiedBy(tokenR);

        this.departmentsService.update(entityToUpdate);

        return ResponseEntity.ok("ok");
    }
}
