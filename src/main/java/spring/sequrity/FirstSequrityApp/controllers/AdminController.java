package spring.sequrity.FirstSequrityApp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.sequrity.FirstSequrityApp.models.Role;
import spring.sequrity.FirstSequrityApp.models.User;
import spring.sequrity.FirstSequrityApp.repository.RoleRepository;
import spring.sequrity.FirstSequrityApp.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return allUsers;
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user,
                                        @RequestParam("rolestring") String rolestring) {
        Set<Role> rolesSet = Arrays.stream(rolestring.split(","))
                .map(String::trim)
                .map(roleName -> roleRepository.findByRole(
                                roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Роль " + roleName + " не найдена")))
                .collect(Collectors.toSet());

        user.setRoles(rolesSet);
        boolean saved = userService.saveUser(user);
        if (saved) {
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Пользователь с таким именем уже существует");
        }
    }

    // Получить пользователя по id
    @GetMapping("/edit/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    @PostMapping ("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id,
                                        @RequestBody User updatedUser,
                                        @RequestParam("roles") String roles) {
        Set<Role> rolesSet = Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(roleName -> roleRepository.findByRole(
                                roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Роль " + roleName + " не найдена")))
                .collect(Collectors.toSet());

        User user = userService.getUserById(id);
        user.setUsername(updatedUser.getUsername());
        user.setPassword(updatedUser.getPassword());
        user.setRoles(rolesSet);
        userService.updateUser(user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
