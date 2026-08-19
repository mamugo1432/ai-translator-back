package es.ia.translator.service;

import es.ia.translator.model.User;
import es.ia.translator.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<User> users = this.userRepository.getUserByUsernameOrEmail(username, username);
        if(users == null || users.size()!=1) throw new UsernameNotFoundException("Credenciales inválidas");
        return users.getFirst();

    }
}
