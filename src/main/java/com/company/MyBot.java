package com.company;

import com.company.container.ComponentContainer;
import com.company.controller.AdminController;
import com.company.database.Database;
import com.company.enums.Language;
import com.company.model.CartProduct;
import com.company.model.Customer;
import com.company.model.Product;
import com.company.service.CartProductService;
import com.company.service.CustomerService;
import com.company.service.ProductService;
import com.company.util.InlineButtonConstants;
import com.company.util.InlineButtonUtil;
import com.company.util.KeyBoardButtonConstants;
import com.company.util.KeyboardButtonUtil;
import com.company.work_files.WorkWithFile;
import org.checkerframework.checker.units.qual.C;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;

public class MyBot extends TelegramLongPollingBot {
    List<String> images = new ArrayList<>();

    public MyBot() {
        images.add("AgACAgIAAxkBAAIGgGJQGHKzY1n9Xi05sLRJkpVxY8eNAAInuzEbXOmAShuMdj0Jb-a6AQADAgADeQADIwQ");
        images.add("AgACAgIAAxkBAAIGiWJQGIaN0tbpguGKMT4dW6B4YWxPAALBxTEbm_hYSnWKw_tlOPnEAQADAgADeQADIwQ");
        images.add("AgACAgIAAxkBAAIGjGJQGKcK5ez6uxgSGfsYCSZJi8NgAAKEvDEbY_OASjgQ7Rjh1EzDAQADAgADeAADIwQ");
    }

    @Override
    public String getBotUsername() {
        return "SHSHSH_first_bot";
    }

    @Override
    public String getBotToken() {
        return "5241413929:AAGsEDEEDmsW6bN3dUwb_uCIa-CMprMxNxM";
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();
            User user = message.getFrom();

//            if(String.valueOf(user.getId()).equals(ComponentContainer.ADMIN)){
//                AdminController.handleMessage(user, message);
//                return;
//            }

            Customer customer = CustomerService.getCustomerById(String.valueOf(user.getId()));

            if (message.hasText()) {

                String text = message.getText();

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));

                if (text.equals("/start")) {

                    if (customer == null) {
                        customer = new Customer(String.valueOf(user.getId()));
                        Database.customerList.add(customer);
                        WorkWithFile.writeToFileCustomers();

                        sendMessage.setText("Tilni tanlang / Выберите язык");
                        sendMessage.setReplyMarkup(InlineButtonUtil.inlineSelectLanguageMarkup());
                    } else {
                        sendMessage.setText("Siz avval START bosgansiz");
                        sendMessage.setReplyMarkup(KeyboardButtonUtil.menuMarkup(customer.getLanguage()));
                    }
                    sendMsg(sendMessage);

                } else if (text.equals(KeyBoardButtonConstants.MENU_UZ) || text.equals(KeyBoardButtonConstants.MENU_RU)) {
                    sendMessage.setText(
                            customer.getLanguage().equals(Language.UZ) ? "Kategoriyalardan birini tanlang:"
                                    : "Пожалуйста, Выберите категорию:"
                    );
                    sendMessage.setReplyMarkup(InlineButtonUtil.categoryMenu(customer.getLanguage()));
                    sendMsg(sendMessage);

                } else if (text.equals(KeyBoardButtonConstants.CART_UZ) || text.equals(KeyBoardButtonConstants.CART_RU)) {

                    showCart(customer);

                } else if (text.equals(KeyBoardButtonConstants.SETTINGS_UZ) || text.equals(KeyBoardButtonConstants.SETTINGS_RU)) {
                    sendMessage.setText(
                            customer.getLanguage().equals(Language.UZ) ? "Tilni o'zgartirish" :
                                    "Выбрать другой язык"
                    );
                    sendMessage.setReplyMarkup(InlineButtonUtil.inlineChangeLanguageMarkup(customer.getLanguage()));
                    sendMsg(sendMessage);
                } else {
                    sendMessage.setText("Men sizni tushunmadim");
                    sendMsg(sendMessage);
                }

                log(user, text);

            } else if (message.hasContact()) {
                Contact contact = message.getContact();

                customer.setFirstName(contact.getFirstName());
                customer.setLastName(contact.getLastName());
                customer.setUsername(user.getUserName());
                customer.setPhoneNumber(contact.getPhoneNumber());
                WorkWithFile.writeToFileCustomers();

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(
                        customer.getLanguage().equals(Language.UZ) ?
                                KeyBoardButtonConstants.MENU_HEADER_UZ :
                                KeyBoardButtonConstants.MENU_HEADER_RU

                );
                sendMessage.setReplyMarkup(KeyboardButtonUtil.menuMarkup(customer.getLanguage()));
                sendMsg(sendMessage);

            } else if (message.hasLocation()) {
                Location location = message.getLocation();
                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                List<CartProduct> cartProductList = CartProductService.getCartProductByCustomer(customer);

                String messageStr = customer.getLanguage().equals(Language.UZ) ? "Savatchada: \n\n" : "В корзине: \n\n";
                Integer total = 0;

                for (CartProduct cartProduct : cartProductList) {
                    if(customer.getLanguage().equals(Language.UZ)){
                        messageStr += cartProduct.getProduct().getNameUz()+"\n";
                    }else{
                        messageStr += cartProduct.getProduct().getNameRu()+"\n";
                    }
                    messageStr += cartProduct.getAmount()+" * "+cartProduct.getProduct().getPrice()+" = "+
                            (cartProduct.getAmount()*cartProduct.getProduct().getPrice())+"\n\n";
                    total += cartProduct.getAmount()*cartProduct.getProduct().getPrice();
                }

                if(customer.getLanguage().equals(Language.UZ)){
                    messageStr += "Jami summa:  "+total;
                }else{
                    messageStr += "Общая сумма:   "+total;
                }

                messageStr += "\n\n";
                //messageStr += "https://yandex.uz/maps/10335/tashkent/?ll="+longitude+"%2C"+latitude+"&z=16";
                messageStr += "<a href=\"https://yandex.uz/maps/10335/tashkent/?ll="+longitude+"%2C"+latitude+"&z=16\"> " +
                        "Xaritadan ko'rish </a>";

                messageStr += "\n\n"+customer.toString();

                AdminController.sendMessageToAdmin(messageStr);

                Database.cartProductList.removeIf(cartProduct -> cartProduct.getCustomer().getId().equals(String.valueOf(message.getChatId())));
                WorkWithFile.writeToFileCartProducts();

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(
                        customer.getLanguage().equals(Language.UZ) ?
                                "Buyurmangiz qabul qilindi. Operatorlarimiz siz bilan tez orada bog'lanishadi." :
                                "Ваш заказ принят. Наши операторы свяжутся с вами в ближайшее время."

                );
                sendMessage.setReplyMarkup(KeyboardButtonUtil.menuMarkup(customer.getLanguage()));
                sendMsg(sendMessage);


            } else if (message.hasAudio()) {

            } else if (message.hasVideo()) {

            } else if (message.hasPhoto()) {
                List<PhotoSize> photoSizeList = message.getPhoto();
                String fileId = photoSizeList.get(photoSizeList.size() - 1).getFileId();

                AdminController.sendPhotoToAdmin(customer.getPhoneNumber()+" "+customer.getId(),
                        fileId);
                System.out.println("fileId = " + fileId);
                images.add(fileId);

                Collections.shuffle(images);

                SendPhoto sendPhoto = new SendPhoto(customer.getId(),
                        new InputFile(images.get(0)));
                sendMsg(sendPhoto);

            } else if (message.hasDocument()) {

            }
        }
        else if (update.hasCallbackQuery()) {
            // clicked inline buttons
            CallbackQuery callbackQuery = update.getCallbackQuery();
            User user = callbackQuery.getFrom();
            Message message = callbackQuery.getMessage();
            String data = callbackQuery.getData();
            System.out.println("data = " + data);

            Customer customer = CustomerService.getCustomerById(String.valueOf(user.getId()));

            if (data.equals(InlineButtonConstants.LANG_UZ_DATA) || data.equals(InlineButtonConstants.LANG_RU_DATA)) {
                customer.setLanguage(
                        data.equals(InlineButtonConstants.LANG_UZ_DATA) ? Language.UZ : Language.RU
                );
                WorkWithFile.writeToFileCustomers();

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()), message.getMessageId()
                );
                sendMsg(deleteMessage);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(
                        customer.getLanguage().equals(Language.UZ) ?
                                "Raqamingizni jo'nating" : "Отправьте свой номер"
                );
                sendMessage.setReplyMarkup(KeyboardButtonUtil.contactMarkup(customer.getLanguage()));
                sendMsg(sendMessage);
            } else if (data.equals(InlineButtonConstants.CHANGE_LANG_DATA)) {
                if (customer.getLanguage().equals(Language.UZ)) {
                    customer.setLanguage(Language.RU);
                } else {
                    customer.setLanguage(Language.UZ);
                }
                WorkWithFile.writeToFileCustomers();

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()), message.getMessageId()
                );
                sendMsg(deleteMessage);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(
                        customer.getLanguage().equals(Language.UZ) ?
                                "Til o'zgartirildi" : "Язык изменен"
                );
                sendMessage.setReplyMarkup(KeyboardButtonUtil.menuMarkup(customer.getLanguage()));
                sendMsg(sendMessage);
            }else if(data.equals("back_from_product_list")){
                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(String.valueOf(message.getChatId()));
                editMessageText.setMessageId(message.getMessageId());
                editMessageText.setText(
                        customer.getLanguage().equals(Language.UZ) ? "Kategoriyalardan birini tanlang:" :
                                "Пожалуйста, Выберите категорию:");

                editMessageText.setReplyMarkup(InlineButtonUtil.categoryMenu(customer.getLanguage()));
                sendMsg(editMessageText);
            }
            else if (data.startsWith("category/")) {
                //   "category/2"
                int categoryId = Integer.parseInt(data.split("/")[1]);

                EditMessageText editMessageText = new EditMessageText();
                editMessageText.setChatId(String.valueOf(message.getChatId()));
                editMessageText.setMessageId(message.getMessageId());
                editMessageText.setText(
                        customer.getLanguage().equals(Language.UZ) ? "Mahsulot tanlang" : "Выберите продукт");

                editMessageText.setReplyMarkup(InlineButtonUtil.productMenu(categoryId, customer.getLanguage()));
                sendMsg(editMessageText);
            }
            else if (data.startsWith("product/")) {
                //   "product/2"
                int productId = Integer.parseInt(data.split("/")[1]);
                Product product = ProductService.getProductById(productId);

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()),message.getMessageId()
                );
                sendMsg(deleteMessage);

                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(String.valueOf(message.getChatId()));
                sendPhoto.setPhoto(new InputFile(new File(product.getImageUrl())));
                sendPhoto.setCaption(
                        "\n"+(customer.getLanguage().equals(Language.UZ) ? product.getNameUz() : product.getNameRu())+": \t" +
                                product.getPrice()
                );
                sendPhoto.setReplyMarkup(InlineButtonUtil.amountMenu(customer.getLanguage(), productId, product.getCategory().getId()));
                sendMsg(sendPhoto);
            }
            else if(data.startsWith("back_from_product_detail/")){

                //   "back_from_product_detail/2"
                int categoryId = Integer.parseInt(data.split("/")[1]);

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()),message.getMessageId()
                );
                sendMsg(deleteMessage);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(
                        customer.getLanguage().equals(Language.UZ) ? "Mahsulot tanlang" : "Выберите продукт");

                sendMessage.setReplyMarkup(InlineButtonUtil.productMenu(categoryId, customer.getLanguage()));
                sendMsg(sendMessage);
            }
            else if(data.startsWith("product_amount/")){

                //   "product_amount/4/8"
                int productId = Integer.parseInt(data.split("/")[1]); // 4
                int amount = Integer.parseInt(data.split("/")[2]);  // 8

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()),message.getMessageId()
                );
                sendMsg(deleteMessage);

                Product product = ProductService.getProductById(productId);
                List<CartProduct> cartProductList = CartProductService.getCartProductByCustomer(customer);

                Optional<CartProduct> optional = cartProductList.stream()
                        .filter(cartProduct -> cartProduct.getProduct().getId().equals(product.getId()))
                        .findFirst();
                if(optional.isPresent()){
                    CartProduct cartProduct = optional.get();
                    cartProduct.setAmount(cartProduct.getAmount()+amount);
                }else{
                    CartProduct cartProduct = new CartProduct(customer, product, amount);
                    Database.cartProductList.add(cartProduct);
                }

                WorkWithFile.writeToFileCartProducts();

                showCart(customer);
            }
            else if(data.startsWith("confirm/")){
                // confirm/213564654
                String customerId = data.split("/")[1]; // 16665

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()),message.getMessageId()
                );
                sendMsg(deleteMessage);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(
                        customer.getLanguage().equals(Language.UZ) ? "Joylashuvingizni jo'nating: " :
                                "Отправьте свое местоположение:"
                );

                sendMessage.setReplyMarkup(KeyboardButtonUtil.locationMarkup(customer.getLanguage()));
                sendMsg(sendMessage);
            }
            else if(data.startsWith("cancel/")){
                // cancel/213564654
                String customerId = data.split("/")[1]; // 16665
                Database.cartProductList.removeIf(cartProduct -> cartProduct.getCustomer().getId().equals(customerId));
                WorkWithFile.writeToFileCartProducts();

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()),message.getMessageId()
                );
                sendMsg(deleteMessage);

                showCart(CustomerService.getCustomerById(customerId));

            }
            else if(data.equals("continue")){
                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()),message.getMessageId()
                );
                sendMsg(deleteMessage);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(String.valueOf(message.getChatId()));
                sendMessage.setText(
                        customer.getLanguage().equals(Language.UZ) ? "Kategoriyalardan birini tanlang:"
                                : "Пожалуйста, Выберите категорию:"
                );
                sendMessage.setReplyMarkup(InlineButtonUtil.categoryMenu(customer.getLanguage()));
                sendMsg(sendMessage);
            }
            else if(data.startsWith("delete_product_from_cart")){
                //   "delete_product_from_cart/16665/8"
                String customerId = data.split("/")[1]; // 16665
                int productId = Integer.parseInt(data.split("/")[2]);  // 8

                Database.cartProductList.removeIf(cartProduct -> cartProduct.getCustomer().getId().equals(customerId)
                && cartProduct.getProduct().getId().equals(productId));
                WorkWithFile.writeToFileCartProducts();

                DeleteMessage deleteMessage = new DeleteMessage(
                        String.valueOf(message.getChatId()),message.getMessageId()
                );
                sendMsg(deleteMessage);

                showCart(CustomerService.getCustomerById(customerId));

            }

        }
        //Database.customerList.forEach(System.out::println);
    }

    private void showCart(Customer customer){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(customer.getId());

        List<CartProduct> cartProductList = CartProductService.getCartProductByCustomer(customer);
        if(cartProductList.isEmpty()){
            sendMessage.setText(customer.getLanguage().equals(Language.UZ) ? "Savat bo'sh" : "Корзина пуста");
            sendMsg(sendMessage);
            return;
        }

        String messageStr = customer.getLanguage().equals(Language.UZ) ? "Savatchada: \n\n" : "В корзине: \n\n";
        Integer total = 0;

        for (CartProduct cartProduct : cartProductList) {
            if(customer.getLanguage().equals(Language.UZ)){
                messageStr += cartProduct.getProduct().getNameUz()+"\n";
            }else{
                messageStr += cartProduct.getProduct().getNameRu()+"\n";
            }
            messageStr += cartProduct.getAmount()+" * "+cartProduct.getProduct().getPrice()+" = "+
                    (cartProduct.getAmount()*cartProduct.getProduct().getPrice())+"\n\n";
            total += cartProduct.getAmount()*cartProduct.getProduct().getPrice();
        }

        if(customer.getLanguage().equals(Language.UZ)){
            messageStr += "Jami summa:  "+total;
        }else{
            messageStr += "Общая сумма:   "+total;
        }

        sendMessage.setText(messageStr);

        sendMessage.setReplyMarkup(InlineButtonUtil.cartMarkup(customer.getLanguage(), customer.getId()));

        sendMsg(sendMessage);
    }

    public void sendMsg(SendPhoto sendPhoto) {
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(EditMessageText editMessageText) {
        try {
            execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(DeleteMessage deleteMessage) {
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void log(User user, String text) {
        String str = String.format("ID: %s, First name: %s, Last name %s, Username %s, Text: %s",
                user.getId(), user.getFirstName(), user.getLastName(), user.getUserName(), text);
        System.out.println(str);
    }


    public void sendMsg(SendDocument sendDocument) {
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
