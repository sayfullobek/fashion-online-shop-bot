package it.UTeam.Onlineshopbot.impl;

import it.UTeam.Onlineshopbot.payload.ProductDto;
import org.springframework.http.HttpEntity;

import java.util.UUID;

public interface ProductControllerImpl {
    HttpEntity<?> getAll();

    HttpEntity<?> create(ProductDto productDto);

    HttpEntity<?> update(UUID id, ProductDto productDto);

    HttpEntity<?> delete(UUID id);

    HttpEntity<?> getOne(UUID id);

    HttpEntity<?> deleteOnePhoto(UUID id);

    HttpEntity<?> getAllByCategoryId(Integer id);
}
