package com.interview.interviewapi.Repository;

import com.interview.interviewapi.Entity.DepartmentsEmployees;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentsEmployeesRepo extends JpaRepository<DepartmentsEmployees, Long> {
    //obtener departamentos por empleado y paginado
    Page<DepartmentsEmployees> findByidEmployee(int id, Pageable pageable);
    //contar departamentos por empleado
    Integer countByidEmployee(int id);
}
