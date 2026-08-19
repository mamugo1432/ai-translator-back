package es.ia.translator.model.repository;

import es.ia.translator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query()
    public List<User> getUserByUsernameOrEmail(String username, String email);
}
