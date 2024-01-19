package it.UTeam.Onlineshopbot.projection;

import it.UTeam.Onlineshopbot.entity.Category;
import org.springframework.data.rest.core.config.Projection;

import java.util.UUID;

@Projection(name = "customCategory", types = Category.class)
public interface CustomCategory {
    Integer getId();

    String getName();

    UUID getPhotoId();
}
