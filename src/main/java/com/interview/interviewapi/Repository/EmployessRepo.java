package com.interview.interviewapi.Repository;

import com.interview.interviewapi.Entity.Employees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployessRepo extends JpaRepository<Employees, Long> {
    //buscar por nombre y paginado
    Page<Employees> findBynameContainingIgnoreCase(String name, Pageable pageable);
    //contar por nombre
    Integer countBynameContainingIgnoreCase(String name);
}
