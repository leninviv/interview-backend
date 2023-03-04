package com.interview.interviewapi.Controller;

import com.interview.interviewapi.Entity.Employees;
import com.interview.interviewapi.Entity.DepartmentsEmployees;
import com.interview.interviewapi.Repository.DepartmentsEmployeesRepo;
import com.interview.interviewapi.Repository.EmployessRepo;
import com.interview.interviewapi.Service.AuthService;
import com.interview.interviewapi.Service.EmployeesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("employees")
public class EmployeesController {
    @Autowired
    private EmployeesService employeesService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmployessRepo repo;

    @Autowired
    private DepartmentsEmployeesRepo repoD;

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
        List<Employees> listaEmployees = this.employeesService.getList(page, limit, search);

        map.put("lista", listaEmployees);
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
        List<Employees> listaEmployees = this.employeesService.getOne(id);
        return ResponseEntity.ok(listaEmployees);
    }

    @PostMapping
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody Employees employees, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);
        Date date = new Date();

        //Set datos de fecha de creacion y auditoria de usuario
        employees.setCreatedDate(date);
        employees.setCreatedBy(tokenR);
        employees.setStatus(true);

        this.employeesService.create(employees);
        return ResponseEntity.ok("ok");
    }

    @PutMapping
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> update(@RequestBody Employees employees, @RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        String name = employees.getName();
        String surname = employees.getSurname();
        int age = employees.getAge();
        String email = employees.getEmail();
        String position = employees.getPosition();
        Boolean status = employees.isStatus();
        Date date = new Date();
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        //set campos a editar y auditoria de usuario
        Employees entityToUpdate = this.repo.getOne(id);
        entityToUpdate.setName(name);
        entityToUpdate.setSurname(surname);
        entityToUpdate.setAge(age);
        entityToUpdate.setEmail(email);
        entityToUpdate.setPosition(position);
        entityToUpdate.setStatus(status);
        entityToUpdate.setModifiedDate(date);
        entityToUpdate.setModifiedBy(tokenR);

        this.employeesService.update(entityToUpdate);

        return ResponseEntity.ok("ok");
    }

    @PostMapping
    @RequestMapping(value = "createdepartment", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createdepartment(@RequestBody DepartmentsEmployees departmentsEmployees, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);
        Date date = new Date();

        //Set datos de fecha de creacion y auditoria de usuario
        departmentsEmployees.setCreatedDate(date);
        departmentsEmployees.setCreatedBy(tokenR);
        departmentsEmployees.setStatus(true);

        this.employeesService.createdepartment(departmentsEmployees);
        return ResponseEntity.ok("ok");
    }

    @GetMapping
    @RequestMapping(value = "readdepartament", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("employee") int employee, @RequestHeader("Authorization") String bearerToken){
        //validacion de token, expirado o invalido. En la variable almacena el username
        String tokenR = authService.validateJWT(bearerToken);

        HashMap<String, List> map = new HashMap<>();
        List<Integer> al = new ArrayList<Integer>();

        //conteo de registros por empleado
        int count = (int) this.repoD.countByidEmployee(employee);
        al.add(0, count);

        //obtener registros
        List<DepartmentsEmployees> listaDepartamentosxEmpleado = this.employeesService.getDepartamentoxEmpleado(page, limit, employee);

        map.put("lista", listaDepartamentosxEmpleado);
        map.put("conteo", al);

        return ResponseEntity.ok(map);
    }

    @DeleteMapping
    @RequestMapping(value = "deletedepartment", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletedeparment(@RequestParam("id") long id, @RequestHeader("Authorization") String bearerToken){
        DepartmentsEmployees departmentsEmployees = this.repoD.getOne(id);
        this.employeesService.deteleDeparment(departmentsEmployees);
        return ResponseEntity.ok("ok");
    }
}
