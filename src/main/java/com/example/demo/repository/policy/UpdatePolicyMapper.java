package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.policy.UpdatePolicyEntity;

@Mapper
public interface UpdatePolicyMapper {
    UpdatePolicyEntity getPolicyDetailsById(int detected_no);
    void updatePolicy(UpdatePolicyEntity policy);
    void updateEnableStatus();
}
