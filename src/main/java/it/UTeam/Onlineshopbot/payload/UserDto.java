package it.UTeam.Onlineshopbot.payload;

import it.UTeam.Onlineshopbot.entity.Role;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private UUID id;

    private String firstName;
    private String lastName;

    private String botFirstName;
    private String botLastName;
    private String botUsername;

    private String chatId;
    private String usernameBot;
    private String token;

    private String username;
    private String password;
    private Set<Role> roles;

    private boolean enabled = true;
    private boolean credentialsNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;

    private String oldPassword;
    private String newPassword;
    private String newPrePassword;

    private String about;

}
