package it.UTeam.Onlineshopbot.repository;

import it.UTeam.Onlineshopbot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsProductByNameEqualsIgnoreCase(String name);
    boolean existsProductByNameEqualsIgnoreCaseAndIdNot(String name);
}
