package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.log.ReadLogsEntity;

@Service
public class LogService {
	
	public String parseBinaryData(ReadLogsEntity log) {
		byte[] packet_bin = log.getPacket_bin();
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : packet_bin) {
			stringBuilder.append(String.format("%02x", b));
		}
		String binaryData = stringBuilder.toString().toUpperCase();
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
}
