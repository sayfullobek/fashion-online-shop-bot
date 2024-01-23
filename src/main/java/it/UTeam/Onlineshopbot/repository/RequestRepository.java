package it.UTeam.Onlineshopbot.repository;

import it.UTeam.Onlineshopbot.entity.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {
}
