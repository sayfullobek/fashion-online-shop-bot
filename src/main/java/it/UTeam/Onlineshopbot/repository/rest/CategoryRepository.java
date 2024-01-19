package it.UTeam.Onlineshopbot.repository.rest;

import it.UTeam.Onlineshopbot.entity.Category;
import it.UTeam.Onlineshopbot.projection.CustomCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "category", collectionResourceRel = "list", excerptProjection = CustomCategory.class)
@CrossOrigin
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
