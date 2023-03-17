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
}