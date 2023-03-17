package com.example.demo.User;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Base64;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String password;
    private String name;
    private String regNo;

    public User() {
    }

    public User(String userId, String password, String name, String regNo) {
        this.userId = userId;
        setPassword(password); // 암호화된 비밀번호 저장
        this.name = name;
        setRegNo(regNo); // 암호화된 주민등록번호 저장
    }
}