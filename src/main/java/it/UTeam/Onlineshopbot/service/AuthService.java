package it.UTeam.Onlineshopbot.service;

import it.UTeam.Onlineshopbot.entity.Users;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.payload.UserDto;
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
    private final PasswordEncoder passwordEncoder;

    public UserDetails getUserById(UUID id) {
        return authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("getUser"));
    }

    public ApiResponse edit(UUID id, UserDto userDto) {
        Users users = authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
        switch (userDto.getAbout()) {
            case "firstNameAndLastName":
                users.setFirstName(userDto.getFirstName());
                users.setLastName(userDto.getLastName());
                break;
            case "username":
                if (!passwordEncoder.encode(userDto.getPassword()).equals(users.getPassword())) {
                    return ApiResponse.builder().message("Parollar mos kelmaydi iltimos qayta urinib ko'ring").success(false).status(401).build();
                }
                users.setUsername(userDto.getUsername());
                break;
            case "password":
                if (!passwordEncoder.encode(userDto.getPassword()).equals(users.getPassword())) {
                    return ApiResponse.builder().message("Xozirgi parolni xato teryapsiz iltimos qayta urinib ko'ring").success(false).status(401).build();
                }
                users.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                break;
            case "bot":
                users.setChatId(userDto.getChatId());
                users.setToken(userDto.getToken());
                users.setUsernameBot(userDto.getUsernameBot());
                break;
        }
        authRepository.save(users);
        return ApiResponse.builder().message("Muvaffaqiyatli bajarildi").success(true).status(200).build();
    }
}

