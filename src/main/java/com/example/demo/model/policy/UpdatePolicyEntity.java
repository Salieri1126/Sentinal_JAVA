package com.example.demo.model.policy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UpdatePolicyEntity {
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
}
