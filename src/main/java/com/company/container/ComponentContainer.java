package com.company.container;

import com.company.MyBot;
import com.company.enums.Language;
import com.company.model.Customer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ComponentContainer {

    public static MyBot MY_TELEGRAM_BOT = null;

    public static final String ADMIN = "";

    public static final File CUSTOMERS_FILE = new File("src/main/resources/customers.json");
    public static final File CART_PRODUCTS_FILE = new File("src/main/resources/cart_products.json");
}
