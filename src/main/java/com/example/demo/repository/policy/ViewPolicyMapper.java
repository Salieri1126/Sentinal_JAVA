package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.policy.ViewPolicyEntity;

@Mapper
public interface ViewPolicyMapper {
	ViewPolicyEntity getPolicyPrintAll(int detected_no);
}
