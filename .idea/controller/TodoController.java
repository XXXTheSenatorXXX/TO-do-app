package com.example.loginandregister.controller;

import com.example.loginandregister.model.Todo;
import com.example.loginandregister.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // Fetch all todos for a logged-in user (e.g., via session or JWT ID)
    @GetMapping("/{userId}")
    public List<Todo> getTodos(@PathVariable Long userId) {
        return todoService.getTodosForUser(userId);
    }

    @PostMapping("/{userId}")
    public Todo createTodo(@PathVariable Long userId, @RequestBody Map<String, String> body) {
        return todoService.addTodo(userId, body.get("title"));
    }

    @PutMapping("/toggle/{id}")
    public Todo toggleTodo(@PathVariable Long id) {
        return todoService.toggleTodo(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
    }

}
