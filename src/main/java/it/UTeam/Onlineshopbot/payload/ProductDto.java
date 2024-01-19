package it.UTeam.Onlineshopbot.payload;

import it.UTeam.Onlineshopbot.entity.Category;
import it.UTeam.Onlineshopbot.entity.Photo;
import it.UTeam.Onlineshopbot.entity.templates.AbsEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto extends AbsEntity {
    private String name;
    private Category category;
    private Integer categoryId;
    private List<Photo> photoIds = new ArrayList<>();
    private UUID photoId;
    private double price;
    private double salePrice;
    private String description;
    private boolean active;

    private String about;
}
