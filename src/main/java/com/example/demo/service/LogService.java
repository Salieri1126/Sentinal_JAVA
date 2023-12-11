package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.log.ReadLogsEntity;

@Service
public class LogService {
	
	public String parseBinaryData(ReadLogsEntity log) {
		String binaryData = new java.math.BigInteger(log.getPacket_bin()).toString(16);
		return parseLogDetail(binaryData);
	}
	
	private String parseLogDetail(String binaryData) {
		return binaryData;
	}
}
