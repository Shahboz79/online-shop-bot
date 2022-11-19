package com.company.service;

import com.company.database.Database;
import com.company.model.Customer;

import java.util.Optional;

public class CustomerService {
    public static Customer getCustomerById(String userId){
        Optional<Customer> optional = Database.customerList.stream()
                .filter(customer -> customer.getId().equals(userId)).findFirst();
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
