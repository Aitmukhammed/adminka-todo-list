package com.company.online_product_shop.controller;

import com.company.online_product_shop.dto.TodoListDTO;
import com.company.online_product_shop.model.TodoList;
import com.company.online_product_shop.service.FilterPriorityService;
import com.company.online_product_shop.service.TodoListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RestController
@RequestMapping("/api/todo")
@Slf4j
public class TodoListController {

    @Autowired
    private TodoListService todoListService;

    @Autowired
    private FilterPriorityService filterPriorityService;

    @PostMapping("/add")
    public ResponseEntity<TodoList> addTodo(@RequestBody TodoList todoList) {
        TodoList todo = new TodoList(todoList.getTitle(), todoList.getDescription(), todoList.getCheckTodo());
        todoListService.save(todo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<TodoListDTO>> getAllTodos() {
        try {
            List<TodoListDTO> todoListDTOS = todoListService.getAllTodos();
            return ResponseEntity.ok(todoListDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoList> getTodo(@PathVariable Long todoId) {
        try {
            TodoList getTodo = todoListService.getTodo(todoId);
            return ResponseEntity.ok(getTodo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{todoId}")
    public ResponseEntity<TodoList> updateTodo(@PathVariable Long todoId, @RequestBody TodoList todoList) {
        try {
            TodoList updateTodo = todoListService.updateTodo(todoId, todoList);
            return ResponseEntity.ok(updateTodo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{todoId}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long todoId) {
        todoListService.deleteTodo(todoId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TodoListDTO>> getPriorities(@RequestParam(value="priorityName") String priorityName) {
        try {
            List<TodoListDTO> todoListDTOS = filterPriorityService.getFilterPriorities(priorityName);
            return ResponseEntity.ok(todoListDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}