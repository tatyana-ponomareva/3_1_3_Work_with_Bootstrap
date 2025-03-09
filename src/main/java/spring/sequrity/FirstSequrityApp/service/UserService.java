package spring.sequrity.FirstSequrityApp.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.sequrity.FirstSequrityApp.models.User;
import spring.sequrity.FirstSequrityApp.repository.RoleRepository;
import spring.sequrity.FirstSequrityApp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public boolean saveUser(User user) {
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb.isPresent()) {
            return false;
        } else {
            userRepository.save(user);
            return true;
        }
    }

    @Transactional
    public User getUserById(Long userId) throws UsernameNotFoundException {
        Optional<User> userFromDb = userRepository.findById(userId);
        if (userFromDb.isPresent()) {
            return userFromDb.get();
        } else {
            throw new UsernameNotFoundException("Пользователь не найден по id: " + userId);
        }
    }

    @Transactional
    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean updateUser(User user) {
        Optional<User> userFromDb = userRepository.findById(user.getId());
        if (userFromDb.isPresent()) {
            User updatingUser = userFromDb.get();
            updatingUser.setUsername(user.getUsername());
            updatingUser.setPassword(passwordEncoder.encode(user.getPassword()));

            updatingUser.setRoles(user.getRoles());

            userRepository.save(updatingUser);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}