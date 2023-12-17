package com.example.demo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
}
