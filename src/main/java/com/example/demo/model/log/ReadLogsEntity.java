package com.example.demo.model.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ReadLogsEntity {
	private Integer log_index;
	private Integer detected_no;
	private String detected_name;
	private LocalDateTime time;
	private Integer action;
	private String detail;
	private String src_ip;
	private String src_port;
	private String dst_ip;
	private byte[] packet_bin;
	private Integer level;

	private String log_date;
	public void setLogdateFromTime() {
        this.setLog_date(this.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
