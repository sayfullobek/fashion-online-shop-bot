package it.UTeam.Onlineshopbot.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse {
    private String message;
    private boolean success;
    private int status;

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
