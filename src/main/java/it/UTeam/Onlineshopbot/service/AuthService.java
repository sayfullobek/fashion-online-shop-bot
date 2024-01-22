package it.UTeam.Onlineshopbot.service;

import it.UTeam.Onlineshopbot.entity.Basket;
import it.UTeam.Onlineshopbot.entity.Users;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.payload.UserDto;
import it.UTeam.Onlineshopbot.repository.AuthRepository;
import it.UTeam.Onlineshopbot.repository.BasketRepository;
import it.UTeam.Onlineshopbot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final BasketRepository basketRepository;

    public UserDetails getUserById(UUID id) {
        return authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("getUser"));
    }

    public ApiResponse registerBotUser(UserDto userDto) {
        Users usersByChatId = authRepository.findUsersByChatId(userDto.getChatId());
        if (usersByChatId == null) {
            Users users = authRepository.save(
                    Users.builder()
                            .lanBot("uz")
                            .chatId(userDto.getChatId())
                            .botFirstName(userDto.getBotFirstName())
                            .botLastName(userDto.getBotLastName())
                            .botUsername(userDto.getBotUsername())
                            .roles(Collections.singleton(roleRepository.findById(2).orElseThrow(() -> new ResourceNotFoundException("getRole"))))
                            .build()
            );
            basketRepository.save(
                    Basket.builder().users(users).productBaskets(null).build()
            );
        }
        return ApiResponse.builder().message("Saqlandi").success(true).status(200).build();
    }

    public ApiResponse changeLan(String chatId, String lan) {
        Users usersByChatId = authRepository.findUsersByChatId(chatId);
        usersByChatId.setLanBot(lan);
        authRepository.save(usersByChatId);
        return ApiResponse.builder().message("Almashdi").success(true).status(200).build();
    }
}

