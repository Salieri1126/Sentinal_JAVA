package com.example.demo.model.policy;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ViewPolicyEntity {
	private Integer detected_no;
    private String detected_name;
    private Integer enable;
    private Integer action;
    private String src_ip;
    private String src_port;
    private String content1;
    private String content2;
    private String content3;
    private Integer base_time;
    private Integer base_limit;
    private Integer level;
    private String detail;
    private String to_sip;
    private String to_sp;
    private String dst_ip;
    private String base_size;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_time;
}