package it.UTeam.Onlineshopbot.service;

import it.UTeam.Onlineshopbot.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthRepository authRepository;

    public UserDetails getUserById(UUID id) {
        return authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("getUser"));
    }
}

