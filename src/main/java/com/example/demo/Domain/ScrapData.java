package com.example.demo.Domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.io.IOException;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ScrapData {
    public ScrapData(String userId, String jsonList) {
        this.userId = userId;
        this.jsonList = formatJson(jsonList);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String name;
    private String regNo;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String jsonList;

    private String 산출세액;
    private String 퇴직연금납입금액;
    private String 보험료납입금액;
    private String 의료비납입금액;
    private String 교육비납입금액;
    private String 기부금납입금액;
    private String 총급여;

    public int getJsonValueAsIntSafely(String key) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonList);
            JsonNode dataNode = rootNode.path("data");
            JsonNode jsonListNode = dataNode.path("jsonList");
            JsonNode targetNode = jsonListNode.path(key);

            String value = targetNode.asText();
            return parseIntSafely(value);
        } catch (IOException | JsonProcessingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int parseIntSafely(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(value.replaceAll(",", ""));
    }

    public String formatJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonString;
        }
    }
}