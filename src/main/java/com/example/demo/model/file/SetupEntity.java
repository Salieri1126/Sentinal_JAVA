package com.example.demo.model.file;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 설정 정보를 담는 엔티티 클래스
 */
@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class SetupEntity {
    private String dbNIC;   // 데이터베이스 NIC
    private String dbIP;    // 데이터베이스 IP
    private String dbPORT;  // 데이터베이스 포트
}
