package it.UTeam.Onlineshopbot.repository;

import it.UTeam.Onlineshopbot.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Users, UUID> {
    Users findUsersByUsername(String username);

    Users findUsersByChatId(String chatId);

    Users findUsersByAdminForId(String adminForId);

    Optional<Users> findUserByUsername(String username);
}
