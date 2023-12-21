package com.example.demo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.policy.InsertPolicyEntity;
import com.example.demo.model.policy.ReadPolicyEntity;
import com.example.demo.model.policy.UpdatePolicyEntity;
import com.example.demo.model.policy.ViewPolicyEntity;
import com.example.demo.repository.policy.DeletePolicyMapper;
import com.example.demo.repository.policy.InsertPolicyMapper;
import com.example.demo.repository.policy.ReadPolicyMapper;
import com.example.demo.repository.policy.UpdatePolicyMapper;
import com.example.demo.repository.policy.ViewPolicyMapper;
import com.example.demo.service.PolicyService;

import lombok.RequiredArgsConstructor;

/**
 * 정책 컨트롤러 클래스
 */
@Controller
@RequiredArgsConstructor
public class PolicyController {

    private final ReadPolicyMapper readPolicyMapper;
    private final InsertPolicyMapper insertPolicyMapper;
    private final UpdatePolicyMapper updatePolicyMapper;
    private final DeletePolicyMapper deletePolicyMapper;
    private final ViewPolicyMapper viewPolicyMapper;
    private final PolicyService policyService;

    /**
     * 정책 목록 화면을 보여주는 메서드
     *
     * @param model Model 객체
     * @return 정책 목록 템플릿 이름
     */
    @GetMapping("/admin/menu/readPolicy")
    public String showPolicyList(Model model) {
        List<ReadPolicyEntity> readPolicies = readPolicyMapper.findAll();
        try {
            for (ReadPolicyEntity policyList : readPolicies) {
                if ("0".equals(policyList.getSrc_ip().trim())) {
                    policyList.setSrc_ip("any");
                }
                if ("0".equals(policyList.getSrc_port().trim())) {
                    policyList.setSrc_port("any");
                }
                if (policyList.getContent1() != null) {
                    policyList.setContent1(policyService.decodingContent(policyList.getContent1()));
                }
                if (policyList.getContent2() != null) {
                    policyList.setContent2(policyService.decodingContent(policyList.getContent2()));
                }
                if (policyList.getContent3() != null) {
                    policyList.setContent3(policyService.decodingContent(policyList.getContent3()));
                }
                if (policyList.getDetail() != null) {
                    policyList.setDetail(policyService.decodingContent(policyList.getDetail()));
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("readPolicies", readPolicies);
        return "readPolicy";
    }

    /**
     * 정책 추가 화면을 보여주는 메서드
     *
     * @return 정책 추가 템플릿 이름
     */
    @GetMapping("/admin/menu/readPolicy/insertPolicy")
    public String showInsertPolicy() {
        return "insertPolicy";
    }

    /**
     * 정책을 추가하는 메서드
     *
     * @param policy 추가할 정책 정보
     * @param model  Model 객체
     * @return 정책 목록 페이지로의 리다이렉트
     */
    @PostMapping("/admin/menu/readPolicy/insertPolicy")
    public String insertPolicy(InsertPolicyEntity policy, Model model) {
        try {
            if ("any".equalsIgnoreCase(policy.getSrc_ip().trim())) {
                policy.setSrc_ip("0");
                policy.setTo_sip("0");
            } else if (policy.getSrc_ip().contains("-")) {
                String[] ips = policyService.isSplit(policy.getSrc_ip());
                policy.setSrc_ip(ips[0]);
                policy.setTo_sip(ips[1]);
            } else {
                policy.setTo_sip("0");
            }
            if ("any".equalsIgnoreCase(policy.getSrc_port().trim())) {
                policy.setSrc_port("0");
                policy.setTo_sp("0");
            } else if (policy.getSrc_port().contains("-")) {
                String[] ports = policyService.isSplit(policy.getSrc_port());
                policy.setSrc_port(ports[0]);
                policy.setTo_sp(ports[1]);
            } else {
                policy.setTo_sp("0");
            }
            if (policy.getContent1() != null) {
                policy.setContent1(policyService.encodingContent(policy.getContent1()));
            }
            if (policy.getContent2() != null) {
                policy.setContent2(policyService.encodingContent(policy.getContent2()));
            }
            if (policy.getContent3() != null) {
                policy.setContent3(policyService.encodingContent(policy.getContent3()));
            }
            if (policy.getDetail() != null) {
                policy.setDetail(policyService.encodingContent(policy.getDetail()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        insertPolicyMapper.insertPolicy(policy);
        List<ReadPolicyEntity> readPolicies = readPolicyMapper.findAll();
        model.addAttribute("readPolicies", readPolicies);
        return "redirect:/admin/menu/readPolicy";
    }

    /**
     * 정책 수정 화면을 보여주는 메서드
     *
     * @param detected_no 수정할 정책의 ID
     * @param model       Model 객체
     * @return 정책 수정 템플릿 이름
     */
    @GetMapping("/admin/menu/readPolicy/updatePolicy")
    public String showUpdatePolicy(@RequestParam("id") int detected_no, Model model) {
        UpdatePolicyEntity policyDetails = updatePolicyMapper.getPolicyDetailsById(detected_no);
        try {
            if ("0".equals(policyDetails.getSrc_ip().trim())) {
                policyDetails.setSrc_ip("any");
            }
            if ("0".equals(policyDetails.getSrc_port().trim())) {
                policyDetails.setSrc_port("any");
            }
            if (policyDetails.getContent1() != null) {
                policyDetails.setContent1(policyService.decodingContent(policyDetails.getContent1()));
            }
            if (policyDetails.getContent2() != null) {
                policyDetails.setContent2(policyService.decodingContent(policyDetails.getContent2()));
            }
            if (policyDetails.getContent3() != null) {
                policyDetails.setContent3(policyService.decodingContent(policyDetails.getContent3()));
            }
            if (policyDetails.getDetail() != null) {
                policyDetails.setDetail(policyService.decodingContent(policyDetails.getDetail()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("policyDetails", policyDetails);
        return "updatePolicy";
    }

    /**
     * 정책을 수정하는 메서드
     *
     * @param detected_no 수정할 정책의 ID
     * @param policy      수정할 정책 정보
     * @param model       Model 객체
     * @return 정책 목록 페이지로의 리다이렉트
     */
    @PostMapping("/admin/menu/readPolicy/updatePolicy")
    public String updatePolicy(@RequestParam("id") int detected_no, UpdatePolicyEntity policy, Model model) {
        try {
            if ("any".equalsIgnoreCase(policy.getSrc_ip().trim())) {
                policy.setSrc_ip("0");
                policy.setTo_sip("0");
            } else if (policy.getSrc_ip().contains("-")) {
                String[] ips = policyService.isSplit(policy.getSrc_ip());
                policy.setSrc_ip(ips[0]);
                policy.setTo_sip(ips[1]);
            } else {
                policy.setTo_sip("0");
            }
            if ("any".equalsIgnoreCase(policy.getSrc_port().trim())) {
                policy.setSrc_port("0");
                policy.setTo_sp("0");
            } else if (policy.getSrc_port().contains("-")) {
                String[] ports = policyService.isSplit(policy.getSrc_port());
                policy.setSrc_port(ports[0]);
                policy.setTo_sp(ports[1]);
            } else {
                policy.setTo_sp("0");
            }
            if (policy.getContent1() != null) {
                policy.setContent1(policyService.encodingContent(policy.getContent1()));
            }
            if (policy.getContent2() != null) {
                policy.setContent2(policyService.encodingContent(policy.getContent2()));
            }
            if (policy.getContent3() != null) {
                policy.setContent3(policyService.encodingContent(policy.getContent3()));
            }
            if (policy.getDetail() != null) {
                policy.setDetail(policyService.encodingContent(policy.getDetail()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        policy.setDetected_no(detected_no);
        updatePolicyMapper.updatePolicy(policy);
        return "redirect:/admin/menu/readPolicy";
    }

    /**
     * 정책 활성/비활성 상태를 업데이트하는 메서드
     *
     * @param policy ReadPolicyEntity 객체
     * @return HTTP 응답 엔터티
     * @throws IOException IOException 예외
     */
    @PostMapping("/admin/menu/readPolicy/updatePolicyEnable")
    public ResponseEntity<Void> updatePolicyEnable(@RequestBody ReadPolicyEntity policy) throws IOException {
        readPolicyMapper.updatePolicyEnable(policy.getDetected_no(), policy.getEnable());
        policyService.sendUDP();
        return ResponseEntity.ok().build();
    }

    /**
     * 정책 삭제 메서드
     *
     * @param detected_no 삭제할 정책의 ID
     * @return 정책 목록 페이지로의 리다이렉트
     * @throws IOException IOException 예외
     */
    @GetMapping("/admin/menu/readPolicy/deletePolicy")
    public String deletePolicy(@RequestParam("id") int detected_no) throws IOException {
        int enableStatus = deletePolicyMapper.getPolicyEnableStatusById(detected_no);
        deletePolicyMapper.deletePolicyById(detected_no);
        if (enableStatus == 1) {
            policyService.sendUDP();
        }
        return "redirect:/admin/menu/readPolicy";
    }

    /**
     * 정책 이름 중복을 체크하는 메서드
     *
     * @param detected_name 정책 이름
     * @return 중복 여부에 따른 문자열
     */
    @GetMapping("/admin/menu/readPolicy/checkDuplication")
    @ResponseBody
    public String checkDuplication(@RequestParam("detected_name") String detected_name) {
        ReadPolicyEntity policy = readPolicyMapper.findByDetectedName(detected_name);
        if (policy != null) {
            return "EXIST";
        } else {
            return "NOT_EXIST";
        }
    }

    /**
     * 정책 상세 정보를 보여주는 메서드
     *
     * @param detected_no 정책의 ID
     * @param model       Model 객체
     * @return 정책 상세 정보 템플릿 이름
     */
    @GetMapping("/admin/menu/readPolicy/viewPolicy")
    public String showViewPolicy(@RequestParam("id") int detected_no, Model model) {
        ViewPolicyEntity viewPolicy = viewPolicyMapper.getPolicyPrintAll(detected_no);
        try {
            if ("0".equals(viewPolicy.getSrc_ip().trim())) {
                viewPolicy.setSrc_ip("any");
            }
            if ("0".equals(viewPolicy.getSrc_port().trim())) {
                viewPolicy.setSrc_port("any");
            }
            if (viewPolicy.getContent1() != null) {
                viewPolicy.setContent1(policyService.decodingContent(viewPolicy.getContent1()));
            }
            if (viewPolicy.getContent2() != null) {
                viewPolicy.setContent2(policyService.decodingContent(viewPolicy.getContent2()));
            }
            if (viewPolicy.getContent3() != null) {
                viewPolicy.setContent3(policyService.decodingContent(viewPolicy.getContent3()));
            }
            if (viewPolicy.getDetail() != null) {
                viewPolicy.setDetail(policyService.decodingContent(viewPolicy.getDetail()));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        model.addAttribute("viewPolicy", viewPolicy);
        return "viewPolicy";
    }
}
