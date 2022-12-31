package com.hanSolo.kinhNguyen.response;

public class QueueSmsResponse {
    private String id;
    private String phone;
    private String text;

    public QueueSmsResponse(String id, String phone, String text) {
        this.id = id;
        this.phone = phone;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
