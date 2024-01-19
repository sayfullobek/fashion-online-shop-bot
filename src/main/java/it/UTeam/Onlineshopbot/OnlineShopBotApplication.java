package it.UTeam.Onlineshopbot;

import it.UTeam.Onlineshopbot.bot.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class OnlineShopBotApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(OnlineShopBotApplication.class, args);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            System.err.println("Not project");
        }
    }

}
