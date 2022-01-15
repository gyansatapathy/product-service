package com.gs.product.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT")
public class Product {

    @Id
    @Column(name = "PRODUCTID")
    private String productId;

    @Column(nullable = false, name = "PRODUCTCATEGORY")
    private String productCategory;

    @Column(name = "PRODUCTNAME", columnDefinition = "VARCHAR(255)")
    private String productName;

    @Column(name = "PRODUCTDESCRIPTION", columnDefinition = "VARCHAR(255)")
    private String productDescription;

    @Column(name = "UNITS")
    private Integer units;

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

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
