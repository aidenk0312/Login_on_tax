package com.example.demo.Domain;

import lombok.*;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Users {
    @Id
    private String userId;
    private String password;
    private String name;
    private String regNo;
}
