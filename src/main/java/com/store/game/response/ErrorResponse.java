package com.store.game.response;

import lombok.Data;

@Data
public class ErrorResponse implements Response{

    private ResponseStatus status = ResponseStatus.error;
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
