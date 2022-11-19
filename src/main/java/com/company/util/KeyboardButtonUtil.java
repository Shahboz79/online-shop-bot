package com.company.util;

import com.company.container.ComponentContainer;
import com.company.enums.Language;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Arrays;
import java.util.List;

public class KeyboardButtonUtil {

    public static ReplyKeyboardMarkup menuMarkup(Language language){
        KeyboardButton menuButton = new KeyboardButton(
                language.equals(Language.UZ) ? KeyBoardButtonConstants.MENU_UZ : KeyBoardButtonConstants.MENU_RU
        );
        KeyboardButton cartButton = new KeyboardButton(
                language.equals(Language.UZ) ? KeyBoardButtonConstants.CART_UZ : KeyBoardButtonConstants.CART_RU
        );
        KeyboardButton settingsButton = new KeyboardButton(
                language.equals(Language.UZ) ? KeyBoardButtonConstants.SETTINGS_UZ : KeyBoardButtonConstants.SETTINGS_RU
        );

        KeyboardRow row1 = new KeyboardRow();
        row1.add(menuButton);

        KeyboardRow row2 = new KeyboardRow();
        row2.add(cartButton);
        row2.add(settingsButton);

        List<KeyboardRow> rowList = Arrays.asList(row1, row2);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        //replyKeyboardMarkup.setOneTimeKeyboard(true);

        replyKeyboardMarkup.setKeyboard(rowList);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup contactMarkup(Language language){
        KeyboardButton contactButton = new KeyboardButton(
                language.equals(Language.UZ) ? KeyBoardButtonConstants.SHARE_CONTACT_UZ :
                        KeyBoardButtonConstants.SHARE_CONTACT_RU
        );
        contactButton.setRequestContact(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(contactButton);

        List<KeyboardRow> rowList = Arrays.asList(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        replyKeyboardMarkup.setKeyboard(rowList);

        return replyKeyboardMarkup;
    }

    public static ReplyKeyboard locationMarkup(Language language) {
        KeyboardButton contactButton = new KeyboardButton(
                language.equals(Language.UZ) ? KeyBoardButtonConstants.SHARE_LOCATION_UZ :
                        KeyBoardButtonConstants.SHARE_LOCATION_RU
        );
        contactButton.setRequestLocation(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add(contactButton);

        List<KeyboardRow> rowList = Arrays.asList(row1);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        replyKeyboardMarkup.setKeyboard(rowList);

        return replyKeyboardMarkup;
    }
}
