package com.interview.interviewapi.Service;

import com.interview.interviewapi.Entity.User;
import com.interview.interviewapi.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo repo;

    public List<User> getList(int page, int limit, String search) {
        Pageable paging = PageRequest.of(page, limit);
        List lista = null;

        //devuelve lista de registros con o sin busqueda
        if(search.length() > 2){
            lista = this.repo.findByusernameContainingIgnoreCase(search, paging).toList();
        }else{
            lista = this.repo.findAll(paging).toList();
        }
        return lista;
    }

    public List<User> getOne(long id){
        //devuelve un registro
        List lista = Collections.singletonList(this.repo.findById(id));

        return lista;
    }

    public void create(User user){
        //ejecutar inmediatemente los comandos sql de insert
        this.repo.saveAndFlush(user);
    }

    public void update(User user){
        //ejecutar inmediatemente los comandos sql de update
        this.repo.saveAndFlush(user);
    }
}
