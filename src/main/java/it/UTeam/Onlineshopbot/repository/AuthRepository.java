package it.UTeam.Onlineshopbot.repository;

import it.UTeam.Onlineshopbot.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository<Users, UUID> {
    Users findUsersByUsername(String username);

    Optional<Users> findUserByUsername(String username);
}
