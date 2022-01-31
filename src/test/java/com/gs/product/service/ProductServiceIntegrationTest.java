package com.gs.product.service;

import com.gs.product.dto.ProductDTO;
import com.gs.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@Rollback
class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testSaveProduct(){
        ProductDTO product = new ProductDTO();
        product.setProductName("test");
        product.setProductId("44");
        product.setProductDescription("Hello");
        product.setUnits(22);
        product.setProductCategory("Jet");

        productService.saveProduct(product);

        assertFalse(productRepository.findAllByProductCategory("Jet").isEmpty());
    }
}


