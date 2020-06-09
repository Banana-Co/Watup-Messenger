package com.buaa.watupmessengeroauthserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "code")
public class VerifyCode {

    private String email;
    private String code;
    private LocalDateTime st;

    public VerifyCode(String email, String code) {
        this.email = email;
        this.code = code;
        this.st = LocalDateTime.now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
