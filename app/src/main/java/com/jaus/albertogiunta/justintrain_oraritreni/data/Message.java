package com.jaus.albertogiunta.justintrain_oraritreni.data;

public class Message {

    private int    priority; // 1 2 3
    private String category; // urgent important ad tip
    private String title;
    private String body;
    private String optionalDetails;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOptionalDetails() {
        return optionalDetails;
    }

    public void setOptionalDetails(String optionalDetails) {
        this.optionalDetails = optionalDetails;
    }

    @Override
    public String toString() {
        return "Message{" +
                "priority=" + priority +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", optionalDetails='" + optionalDetails + '\'' +
                '}';
    }
}