package spring.sequrity.FirstSequrityApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.sequrity.FirstSequrityApp.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.sequrity.FirstSequrityApp.repository.UserRepository;
import spring.sequrity.FirstSequrityApp.security.UserDetails;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository1) {
        this.userRepository = userRepository1;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not found");

        return new UserDetails(user.get());
    }
}
