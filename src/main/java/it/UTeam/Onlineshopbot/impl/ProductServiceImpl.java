package it.UTeam.Onlineshopbot.impl;

import it.UTeam.Onlineshopbot.entity.Product;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.payload.ProductDto;

import java.util.UUID;

public interface ProductServiceImpl {
    ApiResponse create(ProductDto productDto);

    ApiResponse update(UUID id, ProductDto productDto);

    ApiResponse delete(UUID id);

    Product getOne(UUID id);
}
