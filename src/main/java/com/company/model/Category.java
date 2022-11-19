package com.company.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Category {

    private static int generalId = 0;


    private Integer id;
    private String nameUz;
    private String nameRu;

    public Category(String nameUz, String nameRu) {
        this.id = ++generalId;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
    }
}
