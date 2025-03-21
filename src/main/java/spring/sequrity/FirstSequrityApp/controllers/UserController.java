package spring.sequrity.FirstSequrityApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.sequrity.FirstSequrityApp.models.User;
import spring.sequrity.FirstSequrityApp.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/showUser")
    public ResponseEntity<User> showUser(Principal principal) {
        String username = principal.getName();
         User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        return ResponseEntity.ok(user);
    }
}
