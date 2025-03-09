package spring.sequrity.FirstSequrityApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.sequrity.FirstSequrityApp.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
