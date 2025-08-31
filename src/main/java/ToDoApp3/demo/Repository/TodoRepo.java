package ToDoApp3.demo.Repository;

import ToDoApp3.demo.Model.Todo;
import ToDoApp3.demo.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepo extends JpaRepository<Todo, Long> {
    List<Todo> findByUser(User user);

    Todo findByIdAndUser(Long id, User user);

    @Query("SELECT t FROM Todo t " +
            "WHERE t.user = :user " +
            "AND t.completed = false " +
            "ORDER BY t.dueDate ASC")
    List<Todo> findByUserAndActive(User user);
    @Query("SELECT t FROM Todo t " +
            "WHERE t.user = :user " +
            "AND t.completed = true " +
            "ORDER BY t.dueDate ASC")
    List<Todo> findByUserAndCompleted(User user);

    Page<Todo> findByUserAndCompletedFalse(User user, Pageable pageable);
}