package it.UTeam.Onlineshopbot.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReqLogin {
    private String username;
    private String password;
}
