package com.company.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CartProduct {
    private Customer customer;
    private Product product;
    private Integer amount;
}
