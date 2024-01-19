package it.UTeam.Onlineshopbot.controller;

import it.UTeam.Onlineshopbot.entity.Product;
import it.UTeam.Onlineshopbot.impl.ProductControllerImpl;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.payload.ProductDto;
import it.UTeam.Onlineshopbot.repository.ProductRepository;
import it.UTeam.Onlineshopbot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@CrossOrigin
public class ProductController implements ProductControllerImpl {
    private final ProductRepository productRepository;
    private final ProductService productService;

    @GetMapping
    @Override
    public HttpEntity<?> getAll() {
        List<Product> all = productRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    @Override
    public HttpEntity<?> create(@RequestBody ProductDto productDto) {
        ApiResponse apiResponse = productService.create(productDto);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PutMapping("/{id}")
    @Override
    public HttpEntity<?> update(@PathVariable UUID id, @RequestBody ProductDto productDto) {
        ApiResponse update = productService.update(id, productDto);
        return ResponseEntity.status(update.getStatus()).body(update);
    }

    @DeleteMapping("/{id}")
    @Override
    public HttpEntity<?> delete(@PathVariable UUID id) {
        ApiResponse delete = productService.delete(id);
        return ResponseEntity.status(delete.getStatus()).body(delete);
    }

    @GetMapping("/{id}")
    @Override
    public HttpEntity<?> getOne(@PathVariable UUID id) {
        Product one = productService.getOne(id);
        return ResponseEntity.ok(one);
    }

    @DeleteMapping("/photo/{id}")
    @Override
    public HttpEntity<?> deleteOnePhoto(@PathVariable UUID id) {
        ApiResponse apiResponse = productService.deleteOnePhoto(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }
}
