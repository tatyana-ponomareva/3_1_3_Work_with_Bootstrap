package spring.sequrity.FirstSequrityApp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.sequrity.FirstSequrityApp.models.Role;
import spring.sequrity.FirstSequrityApp.models.User;
import spring.sequrity.FirstSequrityApp.repository.RoleRepository;
import spring.sequrity.FirstSequrityApp.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
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
    public String getAllUsers(Model model) {
        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("allUs", allUsers);
        return "allUsers";
    }

    @GetMapping("/new")
    public String getNewUser(Model model) {
        model.addAttribute("user", new User());

        return "newUser";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam("rolestring") String rolestring) {
        Set<Role> rolesSet = Arrays.stream(rolestring.split(","))
                .map(String::trim)
                .map(roleName -> roleRepository.findByRole(
                                roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Роль " + roleName + " не найдена")))
                .collect(Collectors.toSet());

        user.setRoles(rolesSet);
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/edit")
    public String getEditUser(@RequestParam("id") long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "editUser";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam("id") long id,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password,
                             @RequestParam("roles") String roles) {
        Set<Role> rolesSet = Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(roleName -> roleRepository.findByRole(roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName)
                        .orElseThrow(() -> new IllegalArgumentException("Роль " + roleName + " не найдена")))
                .collect(Collectors.toSet());

        User user = userService.getUserById(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setRoles(rolesSet);
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping ("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
