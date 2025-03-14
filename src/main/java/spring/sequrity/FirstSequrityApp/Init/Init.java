package spring.sequrity.FirstSequrityApp.Init;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import spring.sequrity.FirstSequrityApp.models.Role;
import spring.sequrity.FirstSequrityApp.models.User;
import spring.sequrity.FirstSequrityApp.repository.RoleRepository;
import spring.sequrity.FirstSequrityApp.repository.UserRepository;

import java.util.Collections;

@Component
public class Init {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    public Init(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initData() {

        Role roleAdmin = new Role("ROLE_ADMIN");
        roleRepository.save(roleAdmin);
        User admin = new User();
        admin.setUsername("Tanya");
        admin.setPassword("$2y$10$tFuXFOAWE3AY7FgV0HJMN.3xtp.RmUx93j1YNHVPkOf/njKjmcTIm"); //100
        admin.setRoles(Collections.singleton(roleAdmin));
        userRepository.save(admin);

        Role roleUser = new Role("ROLE_USER");
        roleRepository.save(roleUser);
        User user = new User();
        user.setUsername("Ilia");
        user.setPassword("$2y$10$TkRzx2eK/2DtRrH5Y033ve0SmCtDbQEUPyC3RrM5mQtr9iEEqTmiK"); //200
        user.setRoles(Collections.singleton(roleUser));
        userRepository.save(user);

    }
}