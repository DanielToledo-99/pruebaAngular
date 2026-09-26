package com.inventory.product.service;

import com.inventory.product.dto.ProductRequest;
import com.inventory.product.dto.ProductResponse;
import com.inventory.product.entity.Product;
import com.inventory.product.exception.ResourceNotFoundException;
import com.inventory.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> findAll(String name) {
        List<Product> products = StringUtils.hasText(name)
                ? productRepository.findByNameContainingIgnoreCase(name)
                : productRepository.findAll();
        return products.stream().map(ProductResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public ProductResponse findById(Long id) {
        return ProductResponse.fromEntity(getProductOrThrow(id));
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return ProductResponse.fromEntity(productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getProductOrThrow(id);
        applyRequest(product, request);
        return ProductResponse.fromEntity(productRepository.save(product));
    }

    @Override
    public void delete(Long id) {
        Product product = getProductOrThrow(id);
        productRepository.delete(product);
    }

    private Product getProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id " + id));
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setQuantity(request.getQuantity());
        product.setPrice(request.getPrice());
    }
}
