package com.ssg.starroad.user.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class AuthCode {

    @Id
    private String email;
    private int code;

    // 기본 생성자 필요
    protected AuthCode() {}

    public AuthCode(String email, int code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public int getCode() {
        return code;
    }
}