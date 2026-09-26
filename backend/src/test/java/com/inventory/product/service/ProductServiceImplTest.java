package com.inventory.product.service;

import com.inventory.product.dto.ProductRequest;
import com.inventory.product.dto.ProductResponse;
import com.inventory.product.entity.Product;
import com.inventory.product.exception.ResourceNotFoundException;
import com.inventory.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Teclado mecánico");
        product.setDescription("Switches rojos");
        product.setQuantity(10);
        product.setPrice(new BigDecimal("150.00"));
    }

    @Test
    void findAll_sinFiltro_devuelveTodos() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductResponse> result = productService.findAll(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Teclado mecánico");
    }

    @Test
    void findAll_conFiltro_buscaPorNombre() {
        when(productRepository.findByNameContainingIgnoreCase("teclado"))
                .thenReturn(Arrays.asList(product));

        List<ProductResponse> result = productService.findAll("teclado");

        assertThat(result).hasSize(1);
        verify(productRepository, never()).findAll();
    }

    @Test
    void findById_existente_devuelveProducto() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse result = productService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_inexistente_lanzaExcepcion() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_guardaYDevuelveProducto() {
        ProductRequest request = new ProductRequest();
        request.setName("Mouse");
        request.setDescription("Inalámbrico");
        request.setQuantity(5);
        request.setPrice(new BigDecimal("80.00"));

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(2L);
            return p;
        });

        ProductResponse result = productService.create(request);

        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Mouse");
    }

    @Test
    void update_existente_actualizaCampos() {
        ProductRequest request = new ProductRequest();
        request.setName("Teclado actualizado");
        request.setDescription("Switches azules");
        request.setQuantity(20);
        request.setPrice(new BigDecimal("175.00"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse result = productService.update(1L, request);

        assertThat(result.getName()).isEqualTo("Teclado actualizado");
        assertThat(result.getQuantity()).isEqualTo(20);
    }

    @Test
    void update_inexistente_lanzaExcepcion() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.update(1L, new ProductRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_existente_eliminaProducto() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void delete_inexistente_lanzaExcepcion() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
