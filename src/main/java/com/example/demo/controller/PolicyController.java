package com.example.demo.controller;

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
import com.example.demo.repository.policy.DeletePolicyMapper;
import com.example.demo.repository.policy.InsertPolicyMapper;
import com.example.demo.repository.policy.ReadPolicyMapper;
import com.example.demo.repository.policy.UpdatePolicyMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PolicyController {
	
	private final ReadPolicyMapper readPolicyMapper;
	private final InsertPolicyMapper insertPolicyMapper;
	private final UpdatePolicyMapper updatePolicyMapper;
	private final DeletePolicyMapper deletePolicyMapper;
	
	@GetMapping("/admin/menu/readPolicy")
	public String showPolicyList(Model model) {
		System.out.println("!!");
		List<ReadPolicyEntity> readPolicies = readPolicyMapper.findAll();
		
		for (ReadPolicyEntity policyList : readPolicies) {
			if (policyList.getSrc_ip().equals("0")) {
				policyList.setSrc_ip("any");
			}
			if (policyList.getSrc_port().equals("0")) {
				policyList.setSrc_port("any");
			}
		}

		model.addAttribute("readPolicies", readPolicies);
		return "readPolicy";
	}

	@GetMapping("/admin/menu/readPolicy/insertPolicy")
	public String showInsertPolicy(Model model) {
		return "insertPolicy";
	}

	@PostMapping("/admin/menu/readPolicy/insertPolicy")
	public String insertPolicy(InsertPolicyEntity policy, Model model) {
		if ("any".equalsIgnoreCase(policy.getSrc_ip())) {
			policy.setSrc_ip("0");
		}
		if ("any".equalsIgnoreCase(policy.getSrc_port())) {
			policy.setSrc_port("0");
		}

		insertPolicyMapper.insertPolicy(policy);
		List<ReadPolicyEntity> readPolicies = readPolicyMapper.findAll();
		model.addAttribute("readPolicies", readPolicies);
		return "redirect:/admin/menu/readPolicy";
	}

	@GetMapping("/admin/menu/readPolicy/updatePolicy")
	public String showUpdatePolicy(@RequestParam("id") int detected_no, Model model) {
		UpdatePolicyEntity policyDetails = updatePolicyMapper.getPolicyDetailsById(detected_no);

		if (policyDetails.getSrc_ip().equals("0")) {
			policyDetails.setSrc_ip("any");
		}
		if (policyDetails.getSrc_port().equals("0")) {
			policyDetails.setSrc_port("any");
		}

		model.addAttribute("policyDetails", policyDetails);
		return "updatePolicy";
	}
	
	@PostMapping("/admin/menu/readPolicy/updatePolicy")
	public String updatePolicy(@RequestParam("id") int detected_no, UpdatePolicyEntity policy, Model model) {
		if ("any".equalsIgnoreCase(policy.getSrc_ip())) {
	        policy.setSrc_ip("0");
	    }
	    if ("any".equalsIgnoreCase(policy.getSrc_port())) {
	        policy.setSrc_port("0");
	    }

	    policy.setDetected_no(detected_no);
	    updatePolicyMapper.updatePolicy(policy);
	    return "redirect:/admin/menu/readPolicy";
	}
	
	@PostMapping("/admin/menu/readPolicy/updatePolicyEnable")
	public ResponseEntity<Void> updatePolicyEnable(@RequestBody ReadPolicyEntity policy) {
		readPolicyMapper.updatePolicyEnable(policy.getDetected_no(), policy.getEnable());
	    return ResponseEntity.ok().build();
	}

	@GetMapping("/admin/menu/readPolicy/deletePolicy")
	public String deletePolicy(@RequestParam("id") int detected_no) {
		deletePolicyMapper.deletePolicyById(detected_no);
		return "redirect:/admin/menu/readPolicy";
	}
	
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
}
