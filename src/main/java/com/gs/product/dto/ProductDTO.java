package com.gs.product.dto;


import javax.validation.constraints.NotNull;

public class ProductDTO {

    @NotNull(message = "Product ID can't be null")
    private String productID;
    private String productCategory;
    private String productName;
    private String productDescription;
    private int units;


    public String getProductId() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }
}
