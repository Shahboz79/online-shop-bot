package com.company.database;

import com.company.model.CartProduct;
import com.company.model.Category;
import com.company.model.Customer;
import com.company.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Database {
    public static final List<Customer> customerList = new ArrayList<>();
    public static final List<Category> categoryList = new ArrayList<>();
    public static final List<Product> productList = new ArrayList<>();
    public static final List<CartProduct> cartProductList = new ArrayList<>();

    public static void loadData(){
        Category category1 = new Category("Kompyuter", "Компьютер");
        Category category2 = new Category("Televizor", "Телевизор");
        Category category3 = new Category("Telefon", "Телефон");

        Collections.addAll(categoryList, category1, category2, category3);

        Product product1 = new Product("Dell G5","Dell G5", 550, category1,
                "src/main/resources/images/dell.jpg");
        Product product2 = new Product("Acer","Acer", 500, category1,
                "src/main/resources/images/acer.jpg");

        Product product3 = new Product("Artel","Artel", 300, category2,
                "src/main/resources/images/artel.png");
        Product product4 = new Product("Samsung","Samsung", 600, category2,
                "src/main/resources/images/samsung.png");
        Product product5 = new Product("Sony","Sony", 400, category2,
                "src/main/resources/images/sony.jpg");

        Product product6 = new Product("Nokia 220","Nokia 220", 15, category3,
                "src/main/resources/images/nokia.jpg");
        Product product7 = new Product("Samsung S4","Samsung S4", 350, category3,
                "src/main/resources/images/samsung_s4.jpg");
        Product product8 = new Product("Samsung A30","Samsung A30", 250, category3,
                "src/main/resources/images/samsung_a30.jpg");
        Product product9 = new Product("iPhone 13 Pro","iPhone 13 Pro", 1300, category3,
                "src/main/resources/images/iphone_13_pro.jpg");

        Collections.addAll(productList, product1, product2, product3, product4, product5,
                product6, product7, product8, product9);
    }
}
