package com.company.online_product_shop.service;

import com.company.online_product_shop.dto.TodoListDTO;
import com.company.online_product_shop.model.TodoList;
import com.company.online_product_shop.repository.TodoListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterPriorityService extends TodoListService {

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private TodoListService todoListService;
    public List<TodoListDTO> getFilterPriorities(String priorityName) {
        List<TodoList> todoLists = todoListRepository.findByTodoPriorities(priorityName);
        return todoLists.stream()
                .map(todoListService::mapTodoDetails)
                .collect(Collectors.toList());
    }
}
