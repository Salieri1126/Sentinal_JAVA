package com.example.demo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class PolicyService {
	
    public String encodingContent(String content) throws UnsupportedEncodingException {
        return URLEncoder.encode(content, StandardCharsets.UTF_8.toString());
    }
    
    public String decodingContetn(String content) throws UnsupportedEncodingException {
    	return URLDecoder.decode(content, StandardCharsets.UTF_8.toString());
    }
}
