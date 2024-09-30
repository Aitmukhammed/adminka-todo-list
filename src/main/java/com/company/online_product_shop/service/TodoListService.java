package com.company.online_product_shop.service;

import com.company.online_product_shop.dto.TodoListDTO;
import com.company.online_product_shop.model.TodoList;
import com.company.online_product_shop.repository.TodoListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TodoListService {

    @Autowired
    private TodoListRepository todoListRepository;
    public void save(TodoList todoList) {
        todoListRepository.save(todoList);
    }

    public List<TodoListDTO> getAllTodos() {
        List<TodoList> todoLists = todoListRepository.findAll();
        return todoLists.stream()
                .map(this::mapTodoDetails)
                .collect(Collectors.toList());
    }

    public TodoList getTodo(Long todoId) {
        Optional<TodoList> optionalTodo = todoListRepository.findById(todoId);
        if (optionalTodo.isPresent()) {
            TodoList todoList = optionalTodo.get();
            return todoList;
        } else {
            throw new RuntimeException("Todo not found with id: " + todoId);
        }
    }


    public TodoListDTO mapTodoDetails(TodoList todoList) {
        TodoListDTO todoListDTO = new TodoListDTO();
        todoListDTO.setTitle(todoList.getTitle());
        todoListDTO.setDescription(todoList.getDescription());
        todoListDTO.setCheckTodo(todoList.getCheckTodo());
        todoListDTO.setCreatedDate(todoList.getCreatedDate());
        todoListDTO.setDeadline(todoList.getDeadline());
        todoListDTO.setStatus(todoList.getStatus());
        todoListDTO.setTodoPriorities(todoList.getTodoPriorities());
        return todoListDTO;
    }

    public TodoList updateTodo(Long todoId, TodoList updateTodoList) {
        Optional<TodoList> optionalTodo = todoListRepository.findById(todoId);
        if (optionalTodo.isPresent()) {
            TodoList existingTodo = optionalTodo.get();

            existingTodo.setTitle(updateTodoList.getTitle());
            existingTodo.setDescription(updateTodoList.getDescription());
            existingTodo.setCheckTodo(updateTodoList.getCheckTodo());
            existingTodo.setTodoPriorities(updateTodoList.getTodoPriorities());
            existingTodo.setDeadline(updateTodoList.getDeadline());

            if(updateTodoList.getCheckTodo() == true) {
                existingTodo.setStatus("Сделано");
            }

            todoListRepository.save(existingTodo);
            return existingTodo;
        } else {
            throw new RuntimeException("Todo not found with id: " + todoId + " for updating");
        }
    }

    public void deleteTodo(Long todoId) {
        Optional<TodoList> optionalTodo = todoListRepository.findById(todoId);
        if (optionalTodo.isPresent()) {
            TodoList todoList = optionalTodo.get();
            todoListRepository.delete(todoList);
        } else {
            throw new RuntimeException("Todo not found with id: " + todoId);
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkDeadlines() {
        LocalDateTime now = LocalDateTime.now();  // 2024-09-27T13:52:30.111927300
        List<TodoList> timeLists = todoListRepository.findTodoListsByDeadlineBefore(now);

        if (timeLists.isEmpty()) {
            log.info("Нет просроченных задач.");
        } else {
            timeLists.forEach(todo -> {
                if (!"Просрочено".equals(todo.getStatus()) && todo.getCheckTodo() != true) {
                    todo.setStatus("Просрочено");
                    todoListRepository.save(todo);
                    log.info("Статус задачи '" + todo.getTitle() + "' изменен на 'Просрочено'.");
                } else {
                    log.info("Статус задачи '" + todo.getTitle() + "' уже 'Просрочено'.");
                }
            });
        }
    }
}
