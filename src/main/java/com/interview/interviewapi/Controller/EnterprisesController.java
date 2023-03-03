package com.interview.interviewapi.Controller;

import com.interview.interviewapi.Entity.Enterprises;
import com.interview.interviewapi.Repository.EnterprisesRepo;
import com.interview.interviewapi.Service.EnterprisesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("enterprises")
public class EnterprisesController {

    @Autowired
    private EnterprisesService enterprisesService;

    @Autowired
    private EnterprisesRepo repo;

    @GetMapping
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestParam("page") int page, @RequestParam("limit") int limit, @RequestParam("search") String search){
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
    public ResponseEntity<?> readone(@RequestParam("id") long id){
        List<Integer> al = new ArrayList<Integer>();
        List<Enterprises> listaEnterprises = this.enterprisesService.getOne(id);
        return ResponseEntity.ok(listaEnterprises);
    }

}
