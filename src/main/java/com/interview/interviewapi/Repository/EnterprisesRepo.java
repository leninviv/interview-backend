package com.interview.interviewapi.Repository;

import com.interview.interviewapi.Entity.Enterprises;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EnterprisesRepo extends JpaRepository<Enterprises, Long> {
    Page<Enterprises> findBynameContainingIgnoreCase(String name, Pageable pageable);
    Integer countBynameContainingIgnoreCase(String name);
}
