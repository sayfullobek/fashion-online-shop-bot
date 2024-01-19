package it.UTeam.Onlineshopbot.repository;

import it.UTeam.Onlineshopbot.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
}
