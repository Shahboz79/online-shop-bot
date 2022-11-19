package com.company;

import com.company.container.ComponentContainer;
import com.company.database.Database;
import com.company.work_files.WorkWithFile;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {

        Database.loadData();
        WorkWithFile.readFromFileCustomers();
        WorkWithFile.readFromFileCartProducts();

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

            MyBot myBot = new MyBot();
            ComponentContainer.MY_TELEGRAM_BOT = myBot;

            telegramBotsApi.registerBot(myBot);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
