package it.UTeam.Onlineshopbot.bot;

import java.util.Arrays;
import java.util.List;

public interface BotConfig {
    String USERNAME = "onlien_fashion_bot";
    String TOKEN = "6955516191:AAFW5yhFZ5_NslvmmoWFcVJL2gRIibtxWUs";
    List<String> START_BTN = Arrays.asList(
            "Kiyimlarni ko'rish", "Biz bilan bo'glanish", "Sozlamalar"
    );
    List<String> START_BTN_RU = Arrays.asList(
            "Посмотреть одежду", "Связаться с нами", "Настройки"
    );
    List<String> SETTINGS = Arrays.asList(
            "O'zbekcha", "Русский", "Orqaga"
    );
    List<String> SETTINGS_RU = Arrays.asList(
            "O'zbekcha", "Русский", "Назад"
    );
}
