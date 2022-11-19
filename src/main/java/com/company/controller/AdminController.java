package com.company.controller;

import com.company.container.ComponentContainer;
import com.company.database.Database;
import com.company.model.Customer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class AdminController {
    public static void sendPhotoToAdmin(String messageText, String fileId){
        SendPhoto sendPhoto = new SendPhoto(ComponentContainer.ADMIN,
                new InputFile(fileId));
        sendPhoto.setCaption(messageText);
        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendPhoto);
    }

    public static void sendMessageToAdmin(String messageText){
        SendMessage sendMessage = new SendMessage(
                ComponentContainer.ADMIN, messageText
        );
        sendMessage.setParseMode(ParseMode.HTML);
        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
    }

    public static void handleMessage(User user, Message message) {
        if(message.hasText()){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(message.getChatId()));

            String messageStr = "";

            String text = message.getText();
            if(text.equals("/start")){
                messageStr = "Assalomu alaykum, ADMIN aka";
                sendMessage.setText(messageStr);
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }else if(text.equals("/customers")){

                File file = new File("src/main/resources/customers.xlsx");

                try (XSSFWorkbook workbook = new XSSFWorkbook()) {

                    XSSFSheet sheet = workbook.createSheet("customers");

                    XSSFRow headerRow = sheet.createRow(0);
                    headerRow.createCell(0).setCellValue("ID");
                    headerRow.createCell(1).setCellValue("FIRST NAME");
                    headerRow.createCell(2).setCellValue("LAST NAME");
                    headerRow.createCell(3).setCellValue("PHONE NUMBER");

                    int  i = 1;
                    for (Customer customer : Database.customerList) {
                        XSSFRow row = sheet.createRow(i++);
                        row.createCell(0).setCellValue(customer.getId());
                        row.createCell(1).setCellValue(customer.getFirstName());
                        row.createCell(2).setCellValue(customer.getLastName());
                        row.createCell(3).setCellValue(customer.getPhoneNumber());
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    workbook.write(fileOutputStream);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(file.exists()){

                    SendDocument sendDocument = new SendDocument(ComponentContainer.ADMIN,
                            new InputFile(file));
                    ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendDocument);

                    for (Customer customer : Database.customerList) {
                        SendDocument sendDocument1 = new SendDocument(
                                customer.getId(), new InputFile(file)
                        );
                        ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendDocument1);
                    }

                }

            }else{
                messageStr = "Bilmadim";
                sendMessage.setText(messageStr);
                ComponentContainer.MY_TELEGRAM_BOT.sendMsg(sendMessage);
            }
        }
    }
}
