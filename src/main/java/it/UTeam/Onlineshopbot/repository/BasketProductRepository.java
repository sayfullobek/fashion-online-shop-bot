package it.UTeam.Onlineshopbot.repository;

import it.UTeam.Onlineshopbot.entity.ProductBasket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketProductRepository extends JpaRepository<ProductBasket, Integer> {
}
