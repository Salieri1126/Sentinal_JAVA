package com.example.demo.repository.policy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.policy.ReadPolicyEntity;

@Mapper
public interface ReadPolicyMapper {
    List<ReadPolicyEntity> findAll();
    ReadPolicyEntity findByDetectedName(String detected_name);
    void updatePolicyEnable(@Param("detected_no") Integer detected_no, @Param("enable") Integer enable);
}
