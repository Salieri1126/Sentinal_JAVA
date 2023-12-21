package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.demo.model.log.ReadLogsEntity;

@Service
public class LogService {
	
	// 문자열 파라미터 검증 메서드
    public String validateParam(String param) {
        return ("".equalsIgnoreCase(param) || "any".equalsIgnoreCase(param) || param.isEmpty()) ? null : param;
    }
    
    // 정수 파라미터 검증 메서드
    public Integer validateParam(Integer param, Integer defaultValue) {
        return (param == null || param.equals(defaultValue)) ? null : param;
    }
	
	// 16진수 패킷 추출 메서드
	public String getPacket(ReadLogsEntity log) {
		byte[] packet_bin = log.getPacket_bin();
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : packet_bin) {
			stringBuilder.append(String.format("%02X", b));
		}
		String binaryData = stringBuilder.toString().toUpperCase();
		return parseBinaryDataForPacket(binaryData);
	}
	
	// 바이너리 데이터 가공 후 패킷 데이터 생성하는 메서드
	private String parseBinaryDataForPacket(String binaryData) {
		int index = 0;
		String packetData = "";
		while (index < binaryData.length()) {
			String packet = binaryData.substring(index, Math.min(index + 2, binaryData.length()));
			packetData += packet + " ";
			index += 2;
		}
		return packetData.trim();
	}
	
	// 헤더 추출 메서드
	public List<Map<String, String>> getHeader(ReadLogsEntity log) {
		byte[] packet_bin = log.getPacket_bin();
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : packet_bin) {
			stringBuilder.append(String.format("%02x", b));
		}
		String binaryData = stringBuilder.toString().toUpperCase();
		return parseBinaryDataForHeader(binaryData);
	}
	
	private List<Map<String, String>> parseBinaryDataForHeader(String binaryData) {
		int index = 0;
		String packetData = "";
		while (index < binaryData.length()) {
			String packet = binaryData.substring(index, Math.min(index + 2, binaryData.length()));
			packetData += packet + " ";
			index += 2;
		}
		return parseHeader(packetData.trim());
	}
	
	private List<Map<String, String>> parseHeader(String packetData) {
	    // 공백을 제거하고 각 2자리 16진수 문자열을 바이트로 변환
	    String[] hexBytes = packetData.split(" ");
	    byte[] bytes = new byte[hexBytes.length];
	    for (int i = 0; i < hexBytes.length; i++) {
	        bytes[i] = (byte) Integer.parseInt(hexBytes[i], 16);
	    }
	    
	    // Ethernet 헤더값 추출 후 Map 객체에 주입
	    Map<String, String> ethernetHeader = parseEthernetHeader(bytes);
	    
	    // IP 헤더 시작 위치 (Ethernet 헤더는 14바이트)
	    int ipStart = 14;
	    
	    // IP 헤더값 Map 추출 후 객체에 주입
	    Map<String, String> ipHeader = parseIpHeader(bytes, ipStart);
	    
	    // TCP 또는 UDP 헤더 시작 위치 계산 (IP 헤더 길이는 4바이트 단위)
	    int headerLength = Integer.parseInt(ipHeader.get("Header Length"));
	    int tcpOrUdpStart = ipStart + headerLength * 4;
	    
	    // TCP 헤더값 Map 추출 후 객체에 주입
	    Map<String, String> tcpHeader = null;
	    if (ipHeader.get("Datagram Protocol").equals("6")) { // TCP
	        tcpHeader = parseTcpHeader(bytes, tcpOrUdpStart);
	    }
	    
	    List<Map<String, String>> headers = new ArrayList<>();
	    headers.add(ethernetHeader);
	    headers.add(ipHeader);
	    if (tcpHeader != null) {
	        headers.add(tcpHeader);
	    }
	    
	    return headers;
	}
	
	private Map<String, String> parseEthernetHeader(byte[] bytes) {
	    String destinationMac = bytesToHexForMac(Arrays.copyOfRange(bytes, 0, 6));
	    String sourceMac = bytesToHexForMac(Arrays.copyOfRange(bytes, 6, 12));
	    String etherType = bytesToHexForEtherType(Arrays.copyOfRange(bytes, 12, 14));
	    
	    Map<String, String> ethernetHeader = new LinkedHashMap<>();
	    ethernetHeader.put("Destination MAC Addr", destinationMac);
	    ethernetHeader.put("Source MAC Addr", sourceMac);
	    ethernetHeader.put("Ethernet Type", "0x" + etherType);
	    
	    return ethernetHeader;
	}
	
    private String bytesToHexForMac(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X:", b));
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
    
    private String bytesToHexForEtherType(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    private Map<String, String> parseIpHeader(byte[] bytes, int ipStart) {
    	// IP 헤더의 첫 바이트에서 버전과 헤더 길이를 추출
	    byte versionAndHeaderLength = bytes[ipStart];
	    int version = (versionAndHeaderLength >> 4) & 0xF;
	    int headerLength = versionAndHeaderLength & 0xF;
	    
	    // IP 헤더 분석
	    byte typeOfService = bytes[ipStart + 1];
	    int datagramLength = ((bytes[ipStart + 2] & 0xFF) << 8) | (bytes[ipStart + 3] & 0xFF);
	    int identification = ((bytes[ipStart + 4] & 0xFF) << 8) | (bytes[ipStart + 5] & 0xFF);
	    int fragmentOffset = ((bytes[ipStart + 6] & 0x1F) << 8) | (bytes[ipStart + 7] & 0xFF);
	    byte ttl = bytes[ipStart + 8];
	    byte datagramProtocol = bytes[ipStart + 9];
	    int checksumIpHeader = ((bytes[ipStart + 10] & 0xFF) << 8) | (bytes[ipStart + 11] & 0xFF);
	    String sourceIp = (bytes[ipStart + 12] & 0xFF) + "." + (bytes[ipStart + 13] & 0xFF) + "." + (bytes[ipStart + 14] & 0xFF) + "." + (bytes[ipStart + 15] & 0xFF);
	    String destinationIp = (bytes[ipStart + 16] & 0xFF) + "." + (bytes[ipStart + 17] & 0xFF) + "." + (bytes[ipStart + 18] & 0xFF) + "." + (bytes[ipStart + 19] & 0xFF);
	    
	    Map<String, String> ipHeader = new LinkedHashMap<>();
	    ipHeader.put("Version", String.valueOf(version));
	    ipHeader.put("Header Length", String.valueOf(headerLength));
	    ipHeader.put("Type of Service", String.valueOf(typeOfService));
	    ipHeader.put("Datagram Length", String.valueOf(datagramLength));
	    ipHeader.put("Identification", String.valueOf(identification));
	    ipHeader.put("Fragment Offset", String.valueOf(fragmentOffset));
	    ipHeader.put("Time To Live(TTL)", String.valueOf(ttl));
	    ipHeader.put("Datagram Protocol", String.valueOf(datagramProtocol));
	    ipHeader.put("Checksum", String.valueOf(checksumIpHeader));
	    ipHeader.put("Source IP", sourceIp);
	    ipHeader.put("Destination IP", destinationIp);
	    
        return ipHeader;
    }
    
    private Map<String, String> parseTcpHeader(byte[] bytes, int tcpStart) {
        // TCP 헤더 분석
        int sourcePort = ((bytes[tcpStart] & 0xFF) << 8) | (bytes[tcpStart + 1] & 0xFF);
        int destinationPort = ((bytes[tcpStart + 2] & 0xFF) << 8) | (bytes[tcpStart + 3] & 0xFF);
        long sequenceNumber = ((bytes[tcpStart + 4] & 0xFFL) << 24) | ((bytes[tcpStart + 5] & 0xFFL) << 16) | ((bytes[tcpStart + 6] & 0xFFL) << 8) | (bytes[tcpStart + 7] & 0xFFL);
        long acknowledgmentNumber = ((bytes[tcpStart + 8] & 0xFFL) << 24) | ((bytes[tcpStart + 9] & 0xFFL) << 16) | ((bytes[tcpStart + 10] & 0xFFL) << 8) | (bytes[tcpStart + 11] & 0xFFL);
        int dataOffset = (bytes[tcpStart + 12] >> 4) & 0xF;
        int reserved = (bytes[tcpStart + 12] >> 1) & 0x7;
        int flags = bytes[tcpStart + 13] & 0xFF;
        String flagsString = String.format("0x%03X", flags & 0xFF);
        int windowSize = ((bytes[tcpStart + 14] & 0xFF) << 8) | (bytes[tcpStart + 15] & 0xFF);
        int checksumTcpHeader = ((bytes[tcpStart + 16] & 0xFF) << 8) | (bytes[tcpStart + 17] & 0xFF);
        int urgentPointer = ((bytes[tcpStart + 18] & 0xFF) << 8) | (bytes[tcpStart + 19] & 0xFF);
        
        Map<String, String> tcpHeader = new LinkedHashMap<>();
        tcpHeader.put("Source Port", String.valueOf(sourcePort));
        tcpHeader.put("Destination Port", String.valueOf(destinationPort));
        tcpHeader.put("Sequence Number", String.valueOf(sequenceNumber));
        tcpHeader.put("Acknowledgment Number", String.valueOf(acknowledgmentNumber));
        tcpHeader.put("Data Offset", String.valueOf(dataOffset));
        tcpHeader.put("Reserved(RSV)", String.valueOf(reserved));
        tcpHeader.put("flags", flagsString);
        tcpHeader.put("Window Size", String.valueOf(windowSize));
        tcpHeader.put("Checksum", String.valueOf(checksumTcpHeader));
        tcpHeader.put("Urgent Pointer", String.valueOf(urgentPointer));
        
        return tcpHeader;
    }
}
