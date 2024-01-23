package it.UTeam.Onlineshopbot.controller;

import it.UTeam.Onlineshopbot.entity.*;
import it.UTeam.Onlineshopbot.payload.*;
import it.UTeam.Onlineshopbot.repository.*;
import it.UTeam.Onlineshopbot.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final BasketProductRepository basketProductRepository;
    private final RequestRepository requestRepository;

    @GetMapping("/users")
    public HttpEntity<?> getUsersList() {
        List<Users> all = authRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody ReqLogin reqLogin) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        reqLogin.getUsername(), reqLogin.getPassword()
                )
        );
        Users users = authRepository.findUsersByUsername(reqLogin.getUsername());
        if (users == null) {
            return ResponseEntity.status(401).body(ApiResponse.builder().message("Username yoki parolda xatolik").success(false).build());
        }
        return ResponseEntity.status(200).body(new ResToken(jwtTokenProvider.generateToken(users.getId())));
    }

    @GetMapping("/get-user-me/{id}")
    public HttpEntity<?> getUserMe(@PathVariable UUID id) {
        Optional<Users> byId = authRepository.findById(id);
        if (byId.isPresent()) {
            return ResponseEntity.ok(byId.get());
        }
        return ResponseEntity.ok(null);
    }

    public Users getUserByRole() {
        return authRepository.findUsersByAdminForId("adminbek12");
    }

    @PutMapping("/settings/edit/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody UserDto userDto) {
        Users usersId = authRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("getUser"));
        Users users = getUserByRole();
        switch (userDto.getAbout()) {
            case "firstNameAndLastName":
                users.setFirstName(userDto.getFirstName());
                users.setLastName(userDto.getLastName());
                break;
            case "username":
                if (!userDto.getPassword().equals(users.getPasswordSee())) {
                    return ResponseEntity.status(401).body(ApiResponse.builder().message("Parollar mos kelmaydi iltimos qayta urinib ko'ring").success(false).status(401).build());
                }
                users.setUsername(userDto.getUsername());
                break;
            case "password":
                if (!userDto.getOldPassword().equals(users.getPasswordSee())) {
                    return ResponseEntity.status(401).body(ApiResponse.builder().message("Xozirgi parolni xato teryapsiz iltimos qayta urinib ko'ring").success(false).status(401).build());
                }
                users.setPassword(passwordEncoder.encode(userDto.getNewPassword()));
                users.setPasswordSee(userDto.getNewPassword());
                break;
            case "bot":
                users.setChatId(userDto.getChatId());
                users.setToken(userDto.getToken());
                users.setUsernameBot(userDto.getUsernameBot());
                break;
        }
        authRepository.save(users);
        return ResponseEntity.status(200).body(ApiResponse.builder().message("Muvaffaqiyatli bajarildi").success(true).status(200).build());
    }

    @GetMapping("/chat-id/{chatId}")
    public HttpEntity<?> validateUserByChatId(@PathVariable String chatId) {
        Users usersByChatId = authRepository.findUsersByChatId(chatId);
        if (usersByChatId == null) {
            return ResponseEntity.status(404).body(ApiResponse.builder().message("Bunday foydalanuvchi mavjud emas").success(false).status(404).build());
        }
        return ResponseEntity.status(200).body(ApiResponse.builder().message("Muvaffaqiyatli").success(true).status(200).build());
    }

    @PutMapping("/basket-save/{chatId}")
    public HttpEntity<?> saveBaket(@PathVariable String chatId, @RequestBody BasketDto basketDto) {
        Users users = authRepository.findUsersByChatId(chatId);
        if (users == null) {
            return ResponseEntity.status(404).body(ApiResponse.builder().message("Bunday foydalanuvchi mavjud emas").success(false).status(404).build());
        }
        Basket basket = basketRepository.findByUsers(users);
        Product product = productRepository.findById(basketDto.getOneProduct()).orElseThrow(() -> new ResourceNotFoundException("getBasket"));
        ProductBasket productBasket = basketProductRepository.save(
                ProductBasket.builder().product(Collections.singletonList(product)).size(basketDto.getSizeProduct()).build()
        );
        basket.getProductBaskets().add(productBasket);
        double tr = 0;
        for (ProductBasket basketProductBasket : basket.getProductBaskets()) {
            tr = tr + ((basketProductBasket.getProduct().get(0).getPrice() - basketProductBasket.getProduct().get(0).getSalePrice()) * basketProductBasket.getSize());
        }
        basket.setAllPrice(tr);
        basketRepository.save(basket);
        return ResponseEntity.ok("muvaffaqiyatli");
    }

    @GetMapping("/get-basket/by-chat-id/{chatId}")
    public HttpEntity<?> getBasketByChatId(@PathVariable String chatId) {
        Users users = authRepository.findUsersByChatId(chatId);
        if (users == null) {
            return ResponseEntity.status(404).body(ApiResponse.builder().message("Bunday foydalanuvchi mavjud emas").success(false).status(404).build());
        }
        Basket byUsers = basketRepository.findByUsers(users);
        return ResponseEntity.ok(byUsers);
    }

    @GetMapping("/request/get")
    public HttpEntity<?> getAllRequest() {
        List<Request> all = requestRepository.findAll();
        return ResponseEntity.ok(all);
    }

//    @GetMapping("/send-message/{chatId}")
//    public HttpEntity<?> sendMessage(@PathVariable String chatId) throws IOException {
//        StringBuilder sms = new StringBuilder();
//        Users usersByChatId = authRepository.findUsersByChatId(chatId);
//        Basket byUsers = basketRepository.findByUsers(usersByChatId);
//        for (ProductBasket productBasket : byUsers.getProductBaskets()) {
//            Product product = productBasket.getProduct().get(0);
//            sms.append(
//                    product.getName() + "%20" + productBasket.getSize() + "%20X%20" + (product.getPrice() - product.getSalePrice()) + "%20=%20" + (productBasket.getSize() * (product.getPrice() - product.getSalePrice()))
//            );
//        }
//        System.out.println(sms);
//
////        https://api.telegram.org/bot6955516191:AAFW5yhFZ5_NslvmmoWFcVJL2gRIibtxWUs/sendMessage?chat_id=5555360669&text=%3Cp%3Esalom%3C/p%3E&reply_markup={%22inline_keyboard%22:%20[[{%22text%22:%20%22hi%22,%20%22callback_data%22:%20%22hi%22}]]}
//
//
//        String send = "https://api.telegram.org/bot" +
//                BotConfig.TOKEN +
//                "/sendMessage?chat_id=" + chatId +
//                "&text=" + sms.toString() + "%20-%20Umumiy%20narxi%20=%20" + byUsers.getAllPrice() + "&reply_markup=;
//        System.out.println(send);
//        HttpGet httpGet = new HttpGet(send);
//        HttpClients.createDefault().execute(httpGet);
//        return ResponseEntity.status(200).body(ApiResponse.builder().message("Ok").success(true).status(200).build());
//    }
}
