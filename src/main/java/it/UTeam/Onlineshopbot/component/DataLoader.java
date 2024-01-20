package it.UTeam.Onlineshopbot.component;

import it.UTeam.Onlineshopbot.entity.Role;
import it.UTeam.Onlineshopbot.entity.Users;
import it.UTeam.Onlineshopbot.entity.enums.RoleName;
import it.UTeam.Onlineshopbot.repository.AuthRepository;
import it.UTeam.Onlineshopbot.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final AuthRepository authRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String initMode;

    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("create-drop") || initMode.equals("create")) {
            for (RoleName value : RoleName.values()) {
                roleRepository.save(Role.builder().roleName(value).build());
            }
            authRepository.save(
                    Users.builder()
                            .adminForId("adminbek12")
                            .firstName("Admin")
                            .lastName("Adminov")
                            .roles(Collections.singleton(roleRepository.findById(1).orElseThrow(() -> new ResourceNotFoundException("getRole"))))
                            .username("adminjon")
                            .usernameBot("onlien_fashion_bot")
//                            .usernameBot("revolution_8_first_bot")
                            .botUsername("@savdoline_online")
                            .botFirstName("Admin")
                            .botLastName("Adminov")
                            .token("6955516191:AAFW5yhFZ5_NslvmmoWFcVJL2gRIibtxWUs")
                            .chatId("1")
//                            .token("6562603667:AAFonsYgKOijdHlQUsEhcvrmOAGK5pfg7l4")
                            .password(passwordEncoder.encode("root1234"))
                            .passwordSee("root1234")
                            .enabled(true)
                            .accountNonExpired(true)
                            .accountNonLocked(true)
                            .credentialsNonExpired(true)
                            .build()
            );
        }
    }
}
