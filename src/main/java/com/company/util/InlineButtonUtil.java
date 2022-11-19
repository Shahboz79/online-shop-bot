package com.company.util;

import com.company.database.Database;
import com.company.enums.Language;
import com.company.model.CartProduct;
import com.company.model.Category;
import com.company.model.Customer;
import com.company.model.Product;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class InlineButtonUtil {

    public static InlineKeyboardMarkup inlineSelectLanguageMarkup(){

        InlineKeyboardButton uzButton = new InlineKeyboardButton();
        uzButton.setText(InlineButtonConstants.LANG_UZ_DEMO);
        uzButton.setCallbackData(InlineButtonConstants.LANG_UZ_DATA);

        InlineKeyboardButton ruButton = new InlineKeyboardButton();
        ruButton.setText(InlineButtonConstants.LANG_RU_DEMO);
        ruButton.setCallbackData(InlineButtonConstants.LANG_RU_DATA);

        List<InlineKeyboardButton> row = Arrays.asList(uzButton, ruButton);
        List<List<InlineKeyboardButton>> rowList = Arrays.asList(row);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup inlineChangeLanguageMarkup(Language language){

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(
                language.equals(Language.UZ) ? InlineButtonConstants.CHANGE_LANG_DEMO_UZ :
                InlineButtonConstants.CHANGE_LANG_DEMO_RU);

        button.setCallbackData(InlineButtonConstants.CHANGE_LANG_DATA);

        List<InlineKeyboardButton> row = Arrays.asList(button);
        List<List<InlineKeyboardButton>> rowList = Arrays.asList(row);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup categoryMenu(Language language) {

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        for (Category category : Database.categoryList) {

            rowList.add(inlineRow(
                    language.equals(Language.UZ) ? category.getNameUz() : category.getNameRu(),
                    "category/"+category.getId()
            ));
        }

        return new InlineKeyboardMarkup(rowList);
    }

    public static InlineKeyboardMarkup productMenu(int categoryId, Language language) {

        List<Product> productList = Database.productList.stream()
                .filter(product -> product.getCategory().getId() == categoryId)
                .collect(Collectors.toList());

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        for (Product product : productList) {
            rowList.add(inlineRow(
                    language.equals(Language.UZ) ? product.getNameUz() : product.getNameRu(),
                    "product/"+product.getId()
            ));
        }

        rowList.add(inlineRow(
                language.equals(Language.UZ) ? "Ortga qaytish" : "Назад",
                "back_from_product_list"
        ));

        return new InlineKeyboardMarkup(rowList);
    }

    public static List<InlineKeyboardButton> inlineRow(String text, String callBackData){
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callBackData);
        return Arrays.asList(button);
    }

    public static InlineKeyboardMarkup amountMenu(Language language, int productId, int categoryId) {
        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();
        List<InlineKeyboardButton> row = new LinkedList<>();
        for (int i = 1; i <= 9; i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            button.setText(language.equals(Language.UZ) ? (i+" ta") : (i+" шт"));
            button.setCallbackData("product_amount/"+productId+"/"+i);
            row.add(button);

            if(i%3 == 0){
                rowList.add(row);
                row = new LinkedList<>();
            }
        }

        rowList.add(inlineRow(
                language.equals(Language.UZ) ? "Ortga qaytish" : "Назад",
                "back_from_product_detail/"+categoryId
        ));

        return new InlineKeyboardMarkup(rowList);
    }

    public static InlineKeyboardMarkup cartMarkup(Language language, String customerId) {

        List<List<InlineKeyboardButton>> rowList = new LinkedList<>();

        List<CartProduct> cartProductList = Database.cartProductList.stream()
                .filter(cartProduct -> cartProduct.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());

        for (CartProduct cartProduct : cartProductList) {
            Customer customer = cartProduct.getCustomer();
            Product product = cartProduct.getProduct();
            rowList.add(inlineRow(
                    "❌  "+(customer.getLanguage().equals(Language.UZ) ? product.getNameUz() : product.getNameRu()),
                    "delete_product_from_cart/"+customerId+"/"+product.getId()
            ));
        }
        rowList.add(inlineRow(
                (language.equals(Language.UZ) ? "Davom etish" : "Продолжать"),
                "continue"
        ));
        rowList.add(inlineRow(
                (language.equals(Language.UZ) ? "Tasdiqlash" : "Подтверждение"),
                "confirm/"+customerId
        ));
        rowList.add(inlineRow(
                (language.equals(Language.UZ) ? "Bekor qilish" : "Отмена"),
                "cancel/"+customerId
        ));

        return new InlineKeyboardMarkup(rowList);
    }
}
