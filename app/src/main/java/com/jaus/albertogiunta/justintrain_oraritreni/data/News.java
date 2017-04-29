package com.jaus.albertogiunta.justintrain_oraritreni.data;

public class News {

    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "News{" +
                "message='" + message + '\'' +
                '}';
    }
}
