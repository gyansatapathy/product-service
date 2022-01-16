package com.gs.product.mapper;

import com.gs.product.domain.Product;
import com.gs.product.dto.ProductDTO;

public final class ProductDTOFactory {

    private ProductDTOFactory(){
    }

    public static ProductDTO toProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setProductName(product.getProductName());
        productDTO.setProductCategory(product.getProductCategory());
        productDTO.setProductDescription(product.getProductDescription());
        productDTO.setUnits(product.getUnits());

        return productDTO;
    }


    public static Product toProduct(ProductDTO productDTO){
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setProductCategory(productDTO.getProductCategory());
        product.setProductDescription(productDTO.getProductDescription());
        product.setUnits(productDTO.getUnits());

        return product;
    }
}
