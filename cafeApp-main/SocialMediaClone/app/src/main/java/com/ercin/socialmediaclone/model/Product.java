package com.ercin.socialmediaclone.model;

public class Product {

    private String productName;
    private String productPhotoUrl;
    private String productType;
    private String productDescription;

    public Product(String productName, String productType, String productPhotoUrl, String productDescription){

        this.setProductName(productName);
        this.setProductPhotoUrl(productPhotoUrl);
        this.setProductType(productType);
        this.setProductDescription(productDescription);

    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPhotoUrl() {
        return productPhotoUrl;
    }

    public void setProductPhotoUrl(String productPhotoUrl) {
        this.productPhotoUrl = productPhotoUrl;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
}
