package it.UTeam.Onlineshopbot.controller;

import it.UTeam.Onlineshopbot.entity.Users;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.payload.ReqLogin;
import it.UTeam.Onlineshopbot.payload.ResToken;
import it.UTeam.Onlineshopbot.payload.UserDto;
import it.UTeam.Onlineshopbot.repository.AuthRepository;
import it.UTeam.Onlineshopbot.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody ReqLogin reqLogin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        reqLogin.getUsername(), reqLogin.getPassword()
                )
        );
        Users users = authRepository.findUsersByUsername(reqLogin.getUsername());
        if (users == null) {
            return ResponseEntity.status(401).body(ApiResponse.builder().message("Username yoki parolda xatolik").success(false).build());
        }
        return ResponseEntity.status(200).body(new ResToken(jwtTokenProvider.generateToken(users.getId())));
    }

    @GetMapping("/get-user-me/{id}")
    public HttpEntity<?> getUserMe(@PathVariable UUID id) {
        Optional<Users> byId = authRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get());
        }
        return ResponseEntity.ok(null);
    }

    @PutMapping("/settings/edit/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody UserDto userDto) {
        Users users = authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
        switch (userDto.getAbout()) {
            case "firstNameAndLastName":
                users.setFirstName(userDto.getFirstName());
                users.setLastName(userDto.getLastName());
                break;
            case "username":
                if (!userDto.getPassword().equals(users.getPasswordSee())) {
                    return ResponseEntity.status(401).body(ApiResponse.builder().message("Parollar mos kelmaydi iltimos qayta urinib ko'ring").success(false).status(401).build());
                }
                users.setUsername(userDto.getUsername());
                break;
            case "password":
                if (!userDto.getNewPassword().equals(users.getPasswordSee())) {
                    return ResponseEntity.status(401).body(ApiResponse.builder().message("Xozirgi parolni xato teryapsiz iltimos qayta urinib ko'ring").success(false).status(401).build());
                }
                users.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                users.setPasswordSee(userDto.getNewPassword());
                break;
            case "bot":
                users.setChatId(userDto.getChatId());
                users.setToken(userDto.getToken());
                users.setUsernameBot(userDto.getUsernameBot());
                break;
        }
        authRepository.save(users);
        return ResponseEntity.status(200).body(ApiResponse.builder().message("Muvaffaqiyatli bajarildi").success(true).status(200).build());
    }
}
