package com.interview.interviewapi.Service;

import com.interview.interviewapi.Entity.Enterprises;
import com.interview.interviewapi.Repository.EnterprisesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class EnterprisesService {
    @Autowired
    private EnterprisesRepo repo;

    public List<Enterprises> getList(int page, int limit, String search) {
        Pageable paging = PageRequest.of(page, limit);
        List lista = null;
        if(search.length() > 2){
            lista = this.repo.findBynameContainingIgnoreCase(search, paging).toList();
        }else{
            lista = this.repo.findAll(paging).toList();
        }
        return lista;
    }

    public List<Enterprises> getOne(long id){
       List lista = Collections.singletonList(this.repo.findById(id));

        return lista;
    }
}
