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
		int index = 0;
		String packetDetail = "";
		while (index < binaryData.length()) {
			String packet = binaryData.substring(index, Math.min(index + 2, binaryData.length()));
			packetDetail += packet + " ";
			index += 2;
		}
		return packetDetail.trim();
	}
	
	private String parsePacket(String packet) {
		return packet;
	}
}
