package com.example.demo.repository.policy;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.policy.ViewPolicyEntity;

/**
 * 정책 조회를 위한 매퍼 인터페이스
 */
@Mapper
public interface ViewPolicyMapper {

    /**
     * 특정 검출 번호에 해당하는 정책의 전체 정보를 조회하는 메서드
     *
     * @param detected_no  정책 번호
     * @return             검출 번호에 해당하는 정책의 전체 정보
     */
    ViewPolicyEntity getPolicyPrintAll(int detected_no);
    
    /**
     * 주어진 검출 이름에 해당하는 정책 정보를 조회하는 메서드
     *
     * @param detectedName 정책 이름
     * @return             정책 이름에 해당하는 정책 정보
     */
    ViewPolicyEntity findByDetectedName(String detectedName);
}
