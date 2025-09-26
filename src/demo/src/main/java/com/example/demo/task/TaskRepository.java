package com.example.demo.task;

import com.example.demo.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByOwner(User owner);
    List<Task> findByOwnerAndStatus(User owner, TaskStatus status);
    List<Task> findByOwnerAndDataVencimentoBetween(User owner, LocalDate start, LocalDate end);

    // carrega a task já com o owner para evitar LazyInitializationException
    //@Query("select t from Task t join fetch t.owner where t.id = :id")
    //Optional<Task> findByIdWithOwner(@Param("id") Long id);

}
