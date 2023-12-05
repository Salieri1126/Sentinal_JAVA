package com.example.demo.model.file;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ConfFileEntity {
	private String dbNIC;
	private String dbIP;
	private String dbPORT;
}
