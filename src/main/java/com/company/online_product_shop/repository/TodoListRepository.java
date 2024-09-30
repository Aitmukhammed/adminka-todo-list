package com.company.online_product_shop.repository;

import com.company.online_product_shop.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    List<TodoList> findByTodoPriorities(String priorityName);
    @Query("SELECT t FROM TodoList t WHERE t.deadline < :now")
    List<TodoList> findTodoListsByDeadlineBefore(@Param("now") LocalDateTime now);
}

