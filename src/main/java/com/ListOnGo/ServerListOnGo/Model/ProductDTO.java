package com.ListOnGo.ServerListOnGo.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// ProductDTO.java
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String category;
    private String imageUrl;

    public ProductDTO(ProductModel product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.imageUrl = "/product/image/" + product.getId();
    }

}