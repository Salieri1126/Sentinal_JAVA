package com.example.demo.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.repository.policy.UpdatePolicyMapper;

@Service
@EnableScheduling
public class PolicyService {
	
	@Autowired
	private UpdatePolicyMapper updatePolicyMapper;
	
    public String encodingContent(String content) throws UnsupportedEncodingException {
        return URLEncoder.encode(content, StandardCharsets.UTF_8.toString());
    }
    
    public String decodingContent(String content) throws UnsupportedEncodingException {
    	return URLDecoder.decode(content, StandardCharsets.UTF_8.toString());
    }
    
    @Scheduled(cron = "0 0 0 * * *")
    public void updateEnableStatus() {
        updatePolicyMapper.updateEnableStatus();
    }
    
	public String[] isSplit(String portRange) {
	    return portRange.split("~");
	}
    
	public void sendUDP() {
	    final String RECEIVER_HOST = "192.168.1.14";
	    final int RECEIVER_PORT = 9999;
	    final String MESSAGE = "test";

	    try {
	        InetAddress receiverAddress = InetAddress.getByName(RECEIVER_HOST);
	        try (DatagramSocket socket = new DatagramSocket()) {
	            byte[] buffer = MESSAGE.getBytes();
	            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, receiverAddress, RECEIVER_PORT);
	            socket.send(packet);
	        }
	    } catch (UnknownHostException e) {
	        System.err.println("호스트를 찾을 수 없습니다: " + e.getMessage());
	    } catch (IOException e) {
	        System.err.println("I/O 예외가 발생했습니다: " + e.getMessage());
	    }
	}
}
