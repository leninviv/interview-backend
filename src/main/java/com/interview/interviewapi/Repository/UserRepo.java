package com.interview.interviewapi.Repository;

import com.interview.interviewapi.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Integer countByusernameContainingIgnoreCase(String name);
    User getUserByusernameContainingIgnoreCase(String name);
}
