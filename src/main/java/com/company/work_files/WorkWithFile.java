package com.company.work_files;

import com.company.container.ComponentContainer;
import com.company.database.Database;
import com.company.model.CartProduct;
import com.company.model.Customer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

public class WorkWithFile {

    public static void writeToFileCartProducts(){
        try (PrintWriter printWriter = new PrintWriter(ComponentContainer.CART_PRODUCTS_FILE)) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            printWriter.write(gson.toJson(Database.cartProductList));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFileCartProducts(){
        try (BufferedReader reader = new BufferedReader(new FileReader(ComponentContainer.CART_PRODUCTS_FILE))) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Type type = new TypeToken<List<CartProduct>>() {
            }.getType();

            List<CartProduct> cartProductList = gson.fromJson(reader, type);
            Database.cartProductList.addAll(cartProductList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToFileCustomers(){
        try (PrintWriter printWriter = new PrintWriter(ComponentContainer.CUSTOMERS_FILE)) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            printWriter.write(gson.toJson(Database.customerList));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFileCustomers(){
        try (BufferedReader reader = new BufferedReader(new FileReader(ComponentContainer.CUSTOMERS_FILE))) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Type type = new TypeToken<List<Customer>>() {
            }.getType();

            List<Customer> customers = gson.fromJson(reader, type);
            Database.customerList.addAll(customers);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
