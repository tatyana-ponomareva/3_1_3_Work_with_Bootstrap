package spring.sequrity.FirstSequrityApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.sequrity.FirstSequrityApp.models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     Optional<User> findByUsername(String username);
}
