package com.store.game.response;

import lombok.Data;

@Data
public class FailResponse<T> implements Response{
    private ResponseStatus status = ResponseStatus.fail;
    private T data;

    public FailResponse(T data) {
        this.data = data;
    }
}
