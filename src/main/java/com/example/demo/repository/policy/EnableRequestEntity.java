package com.example.demo.repository.policy;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class EnableRequestEntity {
	private Integer detected_no;
    private Integer enable;
}
