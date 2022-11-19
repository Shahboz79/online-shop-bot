package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Product {

    private static int generalId = 0;

    private Integer id;
    private String nameUz;
    private String nameRu;
    private Integer price;
    private Category category;
    private String imageUrl;

    public Product(String nameUz, String nameRu, Integer price, Category category, String imageUrl) {
        id = ++generalId;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.price = price;
        this.category = category;
        this.imageUrl = imageUrl;
    }
}
