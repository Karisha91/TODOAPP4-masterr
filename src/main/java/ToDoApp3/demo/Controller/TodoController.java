package ToDoApp3.demo.Controller;

import ToDoApp3.demo.Dto.TodoRequest;
import ToDoApp3.demo.Model.Todo;
import ToDoApp3.demo.Model.User;
import ToDoApp3.demo.Repository.TodoRepo;
import ToDoApp3.demo.Repository.UserRepo;
import ToDoApp3.demo.Service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private TodoService service;
    private TodoRepo todoRepo;
    private UserRepo userRepo;

    public TodoController(TodoService service, TodoRepo todoRepo, UserRepo userRepo) {
        this.service = service;
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;

    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable Long id) {
        return service.getUserTodoById(id);
    }


    @GetMapping()
    public List<Todo> getUserTodos(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username);
        return service.getUserTodos(user);
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody TodoRequest todoRequest) {
        Todo savedTodo = service.createTodo(todoRequest);
        return ResponseEntity.ok(savedTodo);
    }

    @PatchMapping("{todoId}/toggle")
    public Todo toggleTodoCompletion(@PathVariable Long todoId, Authentication authentication) {
        return service.toggleTodoCompletion(todoId, authentication.getName());
    }

    //@GetMapping("/active")
    //public List<Todo> getActiveTodos(Authentication auth) {
        //return service.getActiveTodos(auth.getName());
   // }

    @GetMapping("/completed")
    public List<Todo> getCompletedTodos(Authentication authentication) {
        return service.getCompletedTodos(authentication.getName());
    }

    @GetMapping("/active")
    public Page<Todo> getActiveTodos(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        User user = userRepo.findByUsername(auth.getName());
        return service.getActiveTodos(user, page, size);
    }

    // Add to your TodoController.java
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody TodoRequest todoRequest, Authentication authentication) {
        Todo updatedTodo = service.updateTodo(id, todoRequest, authentication.getName());
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, Authentication authentication) {
        service.deleteTodo(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}