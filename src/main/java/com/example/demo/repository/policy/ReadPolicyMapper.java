package com.example.demo.repository.policy;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.policy.ReadPolicyEntity;

/**
 * 정책 조회를 위한 매퍼 인터페이스
 */
@Mapper
public interface ReadPolicyMapper {

    /**
     * 모든 정책을 조회하는 메서드
     *
     * @return 모든 정책 리스트
     */
    List<ReadPolicyEntity> findAll();

    /**
     * 검출 이름에 해당하는 정책을 조회하는 메서드
     *
     * @param detected_name 검출 이름
     * @return              검출 이름에 해당하는 정책
     */
    ReadPolicyEntity findByDetectedName(String detected_name);

    /**
     * 특정 검출 번호에 해당하는 정책의 활성 상태를 업데이트하는 메서드
     *
     * @param detected_no 검출 번호
     * @param enable      활성 상태 (1: 활성, 0: 비활성)
     */
    void updatePolicyEnable(@Param("detected_no") Integer detected_no, @Param("enable") Integer enable);
}
