package com.inventory.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.product.dto.ProductRequest;
import com.inventory.product.dto.ProductResponse;
import com.inventory.product.entity.Product;
import com.inventory.product.exception.ResourceNotFoundException;
import com.inventory.product.security.JwtUtil;
import com.inventory.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void findAll_devuelveListaDeProductos() throws Exception {
        when(productService.findAll(null)).thenReturn(Collections.singletonList(sampleResponse()));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Teclado mecánico"));
    }

    @Test
    void create_conNombreVacio_devuelve400ConErroresDeCampo() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("");
        request.setQuantity(-1);
        request.setPrice(BigDecimal.ZERO);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists());
    }

    @Test
    void create_conDatosValidos_devuelve201() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setName("Mouse");
        request.setDescription("Inalámbrico");
        request.setQuantity(5);
        request.setPrice(new BigDecimal("80.00"));

        when(productService.create(any(ProductRequest.class))).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void delete_inexistente_devuelve404() throws Exception {
        doThrow(new ResourceNotFoundException("Producto no encontrado con id 99"))
                .when(productService).delete(eq(99L));

        mockMvc.perform(delete("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void delete_existente_devuelve204() throws Exception {
        doNothing().when(productService).delete(eq(1L));

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    private ProductResponse sampleResponse() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Teclado mecánico");
        product.setDescription("Switches rojos");
        product.setQuantity(10);
        product.setPrice(new BigDecimal("150.00"));
        return ProductResponse.fromEntity(product);
    }
}
