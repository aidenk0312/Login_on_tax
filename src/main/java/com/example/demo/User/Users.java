package com.example.demo.User;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
public class Users {
    @Id
    private String userId;
    private String password;
    private String name;
    private String regNo;

    public Users(String userId, String password, String name, String regNo) {
        this.userId = userId;
        setPassword(password); // 암호화된 비밀번호 저장
        this.name = name;
        setRegNo(regNo); // 암호화된 주민등록번호 저장
    }

    public void setPassword(String password) {
        // 비밀번호 암호화 로직을 여기에 구현하세요.
        this.password = password;
    }

    public void setRegNo(String regNo) {
        // 주민등록번호 암호화 로직을 여기에 구현하세요.
        this.regNo = regNo;
    }
}