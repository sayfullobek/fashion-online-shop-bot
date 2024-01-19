package it.UTeam.Onlineshopbot.entity;

import it.UTeam.Onlineshopbot.entity.templates.AbsEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Product extends AbsEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    private List<Photo> photoId = new ArrayList<>();

    @Column(nullable = false)
    private double price;

    private double salePrice;

    @Column(nullable = false, length = 1000000)
    private String description;

    private boolean active;

}
