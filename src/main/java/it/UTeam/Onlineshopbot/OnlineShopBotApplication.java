package it.UTeam.Onlineshopbot;

import it.UTeam.Onlineshopbot.bot.Bot;
import it.UTeam.Onlineshopbot.repository.AuthRepository;
import it.UTeam.Onlineshopbot.repository.RoleRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class OnlineShopBotApplication {

    public static void main(String[] args) {
        try {
            ConfigurableApplicationContext run = SpringApplication.run(OnlineShopBotApplication.class, args);
            AuthRepository authRepository = run.getBean(AuthRepository.class);
            RoleRepository roleRepository = run.getBean(RoleRepository.class);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot(authRepository, roleRepository));
        } catch (TelegramApiException e) {
            System.err.println("Not project");
        }
    }

}
