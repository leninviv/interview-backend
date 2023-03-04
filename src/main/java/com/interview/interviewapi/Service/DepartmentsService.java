package com.interview.interviewapi.Service;

import com.interview.interviewapi.Entity.Departments;
import com.interview.interviewapi.Entity.Enterprises;
import com.interview.interviewapi.Repository.DepartmentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

@Service
public class DepartmentsService {
    @Autowired
    private DepartmentsRepo repo;

    public List<Departments> getList(int page, int limit, String search) {
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

    public List<Departments> getOne(long id){
        //devuelve un registro
        List lista = Collections.singletonList(this.repo.findById(id));

        return lista;
    }

    public void create(Departments departments){
        //ejecutar inmediatemente los comandos sql de insert
        this.repo.saveAndFlush(departments);
    }

    public void update(Departments departments){
        //ejecutar inmediatemente los comandos sql de update
        this.repo.saveAndFlush(departments);
    }
}
