package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.user.MemberEntity;
import com.example.demo.repository.user.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
    private final MemberMapper memberMapper;
    
    public boolean login(MemberEntity memberEntity) {
        MemberEntity member = memberMapper.findById(memberEntity.getId());
        if (member != null && member.getPassword().equals(memberEntity.getPassword())) {
            return true;
        }
        return false;
    }
}
