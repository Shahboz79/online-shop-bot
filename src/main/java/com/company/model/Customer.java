package com.company.model;

import com.company.enums.Language;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Customer {
    private final String id;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private Language language = Language.UZ;

    public Customer(String userId) {
        this.id = userId;
    }
}
