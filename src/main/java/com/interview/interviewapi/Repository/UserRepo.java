package com.interview.interviewapi.Repository;

import com.interview.interviewapi.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    //contar por username
    Integer countByusernameContainingIgnoreCase(String username);
    //get user bu username
    User getUserByusernameContainingIgnoreCase(String username);
    //buscar por username y paginado
    Page<User> findByusernameContainingIgnoreCase(String username, Pageable pageable);

    //contar por username y omitir el mismo registro de edicion
    @Query("select count(*) from User u where u.username = ?1 and u.id != (?2)")
    long countByusernameEdit(String username, long id);
}
