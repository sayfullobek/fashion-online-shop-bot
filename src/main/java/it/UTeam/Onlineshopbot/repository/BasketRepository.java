package it.UTeam.Onlineshopbot.repository;

import it.UTeam.Onlineshopbot.entity.Basket;
import it.UTeam.Onlineshopbot.entity.Role;
import it.UTeam.Onlineshopbot.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Integer> {
    Basket findByUsers(Users users);
}
