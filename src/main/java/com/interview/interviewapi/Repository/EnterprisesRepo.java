package com.interview.interviewapi.Repository;

import com.interview.interviewapi.Entity.Enterprises;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnterprisesRepo extends JpaRepository<Enterprises, Long> {
    //buscar por nombre y paginado
    Page<Enterprises> findBynameContainingIgnoreCase(String name, Pageable pageable);
    //contar por nombre
    Integer countBynameContainingIgnoreCase(String name);
}
