package com.example.demo.Domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ScrapData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String name;
    private String regNo;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String jsonList;

}