package com.gs.product.service;

import com.gs.product.domain.Product;
import com.gs.product.dto.ProductDTO;
import com.gs.product.exception.DataNotFoundException;
import com.gs.product.mapper.ProductDTOFactory;
import com.gs.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDTOFactory::toProductDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByCategory(String categoryName) {
        return productRepository.findAllByProductCategory(categoryName).stream().map(ProductDTOFactory::toProductDTO).collect(Collectors.toList());
    }

    @Override
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = ProductDTOFactory.toProduct(productDTO);

        return ProductDTOFactory.toProductDTO(productRepository.save(product));
    }

    @Override
    public ProductDTO updateProduct(String productID, ProductDTO productDTO) {
        Product product = productRepository.getById(productID);

        if (product != null) {
            product.setProductCategory(productDTO.getProductCategory());
            product.setProductName(productDTO.getProductName());
            product.setProductDescription(productDTO.getProductDescription());
            product.setUnits(productDTO.getUnits());
            productDTO = ProductDTOFactory.toProductDTO(productRepository.save(product));
        } else {
            throw new DataNotFoundException("Invalid product id" + productID);
        }
        return  productDTO;
    }

    @Override
    public void deleteProduct(String productID) {
        productRepository.deleteById(productID);
    }
}
