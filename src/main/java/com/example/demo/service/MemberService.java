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

	public boolean login(MemberEntity memberEntity) throws NoSuchAlgorithmException {
		MemberEntity member = memberMapper.signin(memberEntity.getId());
		if (member != null) {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest
					.digest((memberEntity.getPassword() + member.getSalt()).getBytes(StandardCharsets.UTF_8));
			String sha256hex = String.format("%064x", new BigInteger(1, hash));
			if (member.getPassword().equals(sha256hex)) {
				return true;
			}
		}
		return false;
	}

	public boolean changePassword(String currentPassword, String newPassword) throws NoSuchAlgorithmException {
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
		return false;
	}

	private String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest((password + salt).getBytes(StandardCharsets.UTF_8));
		String sha256hex = String.format("%064x", new BigInteger(1, hash));
		return sha256hex;
	}
}
