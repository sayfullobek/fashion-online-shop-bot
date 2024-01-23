package it.UTeam.Onlineshopbot.entity;

import it.UTeam.Onlineshopbot.entity.templates.AbsEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Basket extends AbsEntity {
    @OneToOne
    private Users users;

    @ManyToMany
    private List<ProductBasket> productBaskets;

    private double allPrice;
}
