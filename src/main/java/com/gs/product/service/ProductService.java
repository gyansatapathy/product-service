package com.gs.product.service;

import com.gs.product.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    List<ProductDTO> getProductsByCategory(String categoryName);
    ProductDTO saveProduct(ProductDTO productDTO);
    ProductDTO updateProduct(String productID, ProductDTO productDTO);
    void deleteProduct(String productID);
}
