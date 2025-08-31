package ToDoApp3.demo.Repository;

import ToDoApp3.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    User findByUsername(String username);
    Optional<User> findById(Long userId);
}
