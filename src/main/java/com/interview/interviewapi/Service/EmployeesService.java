package com.interview.interviewapi.Service;

import com.interview.interviewapi.Entity.Employees;
import com.interview.interviewapi.Repository.EmployessRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.interview.interviewapi.Entity.DepartmentsEmployees;
import com.interview.interviewapi.Repository.DepartmentsEmployeesRepo;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@Service
public class EmployeesService {
    @Autowired
    private EmployessRepo repo;

    @Autowired
    private DepartmentsEmployeesRepo repoD;

    public List<Employees> getList(int page, int limit, String search) {
        Pageable paging = PageRequest.of(page, limit);
        List lista = null;

        //devuelve lista de registros con o sin busqueda
        if(search.length() > 2){
            lista = this.repo.findBynameContainingIgnoreCase(search, paging).toList();
        }else{
            lista = this.repo.findAll(paging).toList();
        }
        return lista;
    }


    public List<Employees> getOne(long id){
        //devuelve un registro
        List lista = Collections.singletonList(this.repo.findById(id));

        return lista;
    }

    public void create(Employees employees){
        //ejecutar inmediatemente los comandos sql de insert
        this.repo.saveAndFlush(employees);
    }

    public void update(Employees employees){
        //ejecutar inmediatemente los comandos sql de update
        this.repo.saveAndFlush(employees);
    }

    public void createdepartment(DepartmentsEmployees departmentsEmployees){
        //ejecutar inmediatemente los comandos sql de update
        this.repoD.saveAndFlush(departmentsEmployees);
    }

    public List<DepartmentsEmployees> getDepartamentoxEmpleado(int page, int limit, int id){
        Pageable paging = PageRequest.of(page, limit);
        List lista = null;

        lista = this.repoD.findByidEmployee(id, paging).toList();

        return lista;
    }

    public void deteleDeparment(DepartmentsEmployees departmentsEmployees){
        this.repoD.delete(departmentsEmployees);
    }
}
