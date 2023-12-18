package com.example.demo.repository.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.user.MemberEntity;

@Mapper
public interface MemberMapper {
    MemberEntity signin(@Param("id") String id);
	void updatePassword(MemberEntity member);
}
