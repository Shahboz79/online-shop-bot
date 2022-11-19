package com.company.service;

import com.company.database.Database;
import com.company.model.CartProduct;
import com.company.model.Customer;
import com.company.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartProductService {
    public static List<CartProduct> getCartProductByCustomer(Customer customer){
        return Database.cartProductList.stream()
                .filter(cartProduct -> cartProduct.getCustomer().getId().equals(customer.getId()))
                .collect(Collectors.toList());
    }
}
