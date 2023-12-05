package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.policy.InsertPolicyEntity;

@Mapper
public interface InsertPolicyMapper {
    void insertPolicy(InsertPolicyEntity policy);
}
