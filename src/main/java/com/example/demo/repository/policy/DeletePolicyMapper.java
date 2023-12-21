package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeletePolicyMapper {
    void deletePolicyById(int detected_no);
	int getPolicyEnableStatusById(int detected_no);
}
