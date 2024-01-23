package it.UTeam.Onlineshopbot.bot;

import it.UTeam.Onlineshopbot.entity.Users;
import it.UTeam.Onlineshopbot.payload.ApiResponse;
import it.UTeam.Onlineshopbot.payload.UserDto;
import it.UTeam.Onlineshopbot.repository.AuthRepository;
import it.UTeam.Onlineshopbot.repository.RoleRepository;
import it.UTeam.Onlineshopbot.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {
    private final AuthRepository authRepository;
    private final AuthService authService;
    private final RoleRepository roleRepository;

    public Users getUserByRole() {
        return authRepository.findUsersByAdminForId("adminbek12");
    }

    public Users getUserByChatId(String chatId) {
        return authRepository.findUsersByChatId(chatId);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            User from = message.getFrom();
            if (message.hasText()) {
                String text = message.getText();
                if (text.equals("/start")) {
                    authService.registerBotUser(UserDto.builder().chatId(chatId).botFirstName(from.getFirstName()).botLastName(from.getLastName()).botUsername(from.getUserName()).build());
                    getBtn(chatId, getUserByChatId(chatId).getLanBot().equals("uz") ? "Assalomu aleykum bizning online do'konimizga hush kelibsiz" : "Здравствуйте, добро пожаловать в наш интернет-магазин", BotConfig.START_BTN);
                } else if (BotConfig.START_BTN.get(1).equals(text) || BotConfig.START_BTN_RU.get(1).equals(text)) {
                    sendMsg(chatId, getUserByChatId(chatId).getLanBot().equals("uz") ? "Murojat uchun " + getUserByRole().getBotUsername() + " ushbu profilga murojat qiling" : "Чтобы подать заявку " + getUserByRole().getBotUsername() + " примените к этому профилю", 0);
                } else if (BotConfig.START_BTN.get(2).equals(text) || BotConfig.START_BTN_RU.get(2).equals(text)) {
                    getBtn(chatId, getUserByChatId(chatId).getLanBot().equals("uz") ? "Sozlamalar" : "Настройки", getUserByChatId(chatId).getLanBot().equals("uz") ? BotConfig.SETTINGS : BotConfig.SETTINGS_RU);
                } else if (text.equals(BotConfig.SETTINGS.get(2)) || text.equals(BotConfig.SETTINGS_RU.get(2))) {
                    getBtn(chatId, getUserByChatId(chatId).getLanBot().equals("uz") ? "Asosiy bo'limga qaytdingiz" : "Вы вернулись в основной раздел", getUserByChatId(chatId).getLanBot().equals("uz") ? BotConfig.START_BTN : BotConfig.START_BTN_RU);
                } else if (text.equals(BotConfig.SETTINGS.get(0))) {
                    authService.changeLan(chatId, "uz");
                    getBtn(chatId, "Assalomu aleykum bizning online do'konimizga hush kelibsiz", BotConfig.START_BTN);
                } else if (text.equals(BotConfig.SETTINGS.get(1))) {
                    authService.changeLan(chatId, "ru");
                    getBtn(chatId, "Здравствуйте, добро пожаловать в наш интернет-магазин", BotConfig.START_BTN_RU);
                }
            } else if (message.hasContact()) {
                Contact contact = message.getContact();
                if (BotConfig.IS.get(chatId) != null) {
                    if (BotConfig.IS.get(chatId).equals("phone")) {
                        ApiResponse apiResponse = authService.request(chatId, contact.getPhoneNumber());
                        sendMsg(chatId, getUserByChatId(chatId).getLanBot().equals("uz") ? "Hurmatli mijoz sizning buyurtmangiz qabul qilindi tez orada sizga managerlarimiz a'loqaga chiqishadi. Buyurtmangizning raqami " + apiResponse.getMessage() : "Уважаемый покупатель, Ваш заказ принят и наши менеджеры свяжутся с Вами в ближайшее время. Номер вашего заказа " + apiResponse.getMessage(), 0);
                        BotConfig.IS.remove(chatId);
                        getBtn(chatId, getUserByChatId(chatId).getLanBot().equals("uz") ? "Assalomu aleykum bizning online do'konimizga hush kelibsiz" : "Здравствуйте, добро пожаловать в наш интернет-магазин", BotConfig.START_BTN);
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            String chatId = callbackQuery.getFrom().getId().toString();
            String[] da = data.split(" : ");
            if (da[0].equals("sotib olaman")) {
                sendMsg(chatId, getUserByChatId(chatId).getLanBot().equals("uz") ? "Telefon raqamingizni ulashing iltimos" : "Поделитесь, пожалуйста, своим номером телефона", 0);
                BotConfig.IS.put(chatId, "phone");
            }
        }
    }

    @Override
    public String getBotToken() {
        return BotConfig.TOKEN;
    }


    @Override
    public String getBotUsername() {
        return BotConfig.USERNAME;
    }

    public void sendMsg(String chatId, String text, Integer messageId) {
        SendMessage sendMessage = new SendMessage();
        if (messageId == 0) {
            sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
        } else {
            sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .build();
        }
        try {
            execute(
                    sendMessage
            );
        } catch (TelegramApiException e) {
            System.err.println("Not bot");
        }
    }

    public void getBtn(String chatId, String text, List<String> btns) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> rows = new ArrayList<>();
        int tr = 0;
        for (int i = 0; i < btns.size() / 2; i++) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j < 2; j++) {
                KeyboardButton build = new KeyboardButton();
                if (btns.get(tr).equals("Kiyimlarni ko'rish")) {
                    build.setText(btns.get(tr));
                    build.setWebApp(WebAppInfo.builder().url("https://main--melodious-chimera-10eafb.netlify.app/" + chatId).build());
                } else {
                    build.setText(btns.get(tr));
                }
                row.add(build);
                tr++;
            }
            rows.add(row);
        }
        if (btns.size() % 2 != 0) {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton build = KeyboardButton.builder()
                    .text(btns.get(btns.size() - 1))
                    .build();
            row.add(build);
            rows.add(row);
        }
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        try {
            execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .replyMarkup(replyKeyboardMarkup)
                            .build()
            );
        } catch (TelegramApiException e) {
            System.err.println("Not Btn");
        }
    }
}
