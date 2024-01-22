package it.UTeam.Onlineshopbot.payload;

import it.UTeam.Onlineshopbot.entity.Product;
import it.UTeam.Onlineshopbot.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BasketDto {
    private Users users;
    private Set<Product> products;
    private UUID oneProduct;
    private Integer sizeProduct;
}
