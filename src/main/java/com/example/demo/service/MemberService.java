package com.example.demo.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.example.demo.model.user.MemberEntity;
import com.example.demo.repository.user.MemberMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberMapper memberMapper;
	
    public boolean login(MemberEntity memberEntity) {
        try {
            MemberEntity member = memberMapper.signin(memberEntity.getId());
            if (member != null) {
                String sha256hex = hashPassword(memberEntity.getPassword(), member.getSalt());
                if (member.getPassword().equals(sha256hex)) {
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("암호화 알고리즘이 없습니다: " + e.getMessage());
        }
        return false;
    }

    public boolean changePassword(String currentPassword, String newPassword) {
        try {
            final String id = "admin01";
            MemberEntity memberEntity = memberMapper.signin(id);
            if (memberEntity != null) {
                String hashedCurrentPassword = hashPassword(currentPassword, memberEntity.getSalt());
                if (memberEntity.getPassword().equals(hashedCurrentPassword)) {
                    String hashedNewPassword = hashPassword(newPassword, memberEntity.getSalt());
                    memberEntity.setPassword(hashedNewPassword);
                    memberMapper.updatePassword(memberEntity);
                    return true;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.err.println("암호화 알고리즘이 없습니다: " + e.getMessage());
        }
        return false;
    }

    private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
        return String.format("%064x", new BigInteger(1, hash));
    }
}
