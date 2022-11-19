package com.company.service;

import com.company.database.Database;
import com.company.model.Product;

import java.util.Optional;

public class ProductService {
    public static Product getProductById(Integer id){
        Optional<Product> optional = Database.productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
        return optional.get();
    }
}
