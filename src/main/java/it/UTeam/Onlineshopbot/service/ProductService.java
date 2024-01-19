package it.UTeam.Onlineshopbot.service;

import it.UTeam.Onlineshopbot.entity.Photo;
import it.UTeam.Onlineshopbot.entity.Product;
import it.UTeam.Onlineshopbot.impl.ProductServiceImpl;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.payload.ProductDto;
import it.UTeam.Onlineshopbot.repository.PhotoRepository;
import it.UTeam.Onlineshopbot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceImpl {
    private final ProductRepository productRepository;
    private final PhotoRepository photoRepository;

    @Override
    public ApiResponse create(ProductDto productDto) {
        if (productRepository.existsProductByNameEqualsIgnoreCase(productDto.getName())) {
            return ApiResponse.builder().message("Bunday mahsulot avvaldan mavjud").success(false).status(409).build();
        }
        productRepository.save(
                Product.builder()
                        .name(productDto.getName())
                        .price(productDto.getPrice())
                        .salePrice(0)
                        .description(productDto.getDescription())
                        .active(false)
                        .build()
        );
        return ApiResponse.builder().message("Muvaffaqiyatli saqlandi").success(true).status(200).build();
    }

    @Override
    public ApiResponse update(UUID id, ProductDto productDto) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()) {
            Product product = byId.get();
            switch (productDto.getAbout()) {
                case "price":
                    product.setSalePrice(product.getSalePrice());
                    break;
                case "photo":
                    Photo save = photoRepository.save(Photo.builder().photoId(productDto.getPhotoId()).build());
                    product.getPhotoId().add(save);
                    break;
                case "other":
                    product.setName(productDto.getName());
                    product.setPrice(product.getPrice());
                    product.setDescription(product.getDescription());
                    break;
            }
            productRepository.save(product);
            return ApiResponse.builder().message("Muvaffaqiyatli o'zgartirildi").success(true).status(200).build();
        }
        return ApiResponse.builder().message("Bunday mahsulot mavjud emas").success(false).status(404).build();
    }

    @Override
    public ApiResponse delete(UUID id) {
        productRepository.delete(productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getProduct")));
        return ApiResponse.builder().message("Muvaffaqiyatli o'chirildi").success(true).status(200).build();
    }

    @Override
    public Product getOne(UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getProduct"));
    }
}
