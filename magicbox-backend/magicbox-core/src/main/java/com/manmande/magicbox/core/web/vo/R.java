package com.manmande.magicbox.core.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {
    private String state;

    private String message;

    private List<String> logs;

    private T data;


    public static <T> R<T> success(T data) {
        R<T> response = new R<>();
        response.setState("success");
        response.setData(data);
        return response;
    }

    public static <T> R<T> error(String error) {
        R<T> response = new R<>();
        response.setState("error");
        response.setMessage(error);
        return response;
    }
}
