package com.example.demo.controller;

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

@Controller
@RequiredArgsConstructor
public class PolicyController {

	private final ReadPolicyMapper readPolicyMapper;
	private final InsertPolicyMapper insertPolicyMapper;
	private final UpdatePolicyMapper updatePolicyMapper;
	private final DeletePolicyMapper deletePolicyMapper;
	private final ViewPolicyMapper viewPolicyMapper;
	private final PolicyService policyService;
	
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
				if(policyList.getContent1() != null) {
					policyList.setContent1(policyService.decodingContent(policyList.getContent1()));
		        }
		        if(policyList.getContent2() != null) {
		        	policyList.setContent2(policyService.decodingContent(policyList.getContent2()));
		        }
		        if(policyList.getContent3() != null) {
		        	policyList.setContent3(policyService.decodingContent(policyList.getContent3()));
		        }
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("readPolicies", readPolicies);
		return "readPolicy";
	}

	@GetMapping("/admin/menu/readPolicy/insertPolicy")
	public String showInsertPolicy() {
		return "insertPolicy";
	}

	@PostMapping("/admin/menu/readPolicy/insertPolicy")
	public String insertPolicy(InsertPolicyEntity policy, Model model) {
	    try {
	        if ("any".equalsIgnoreCase(policy.getSrc_ip().trim())) {
	            policy.setSrc_ip("0");
	        }
	        if ("any".equalsIgnoreCase(policy.getSrc_port().trim())) {
	            policy.setSrc_port("0");
	        }
	        if(policy.getContent1() != null) {
	            policy.setContent1(policyService.encodingContent(policy.getContent1()));
	        }
	        if(policy.getContent2() != null) {
	            policy.setContent2(policyService.encodingContent(policy.getContent2()));
	        }
	        if(policy.getContent3() != null) {
	            policy.setContent3(policyService.encodingContent(policy.getContent3()));
	        }
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
        insertPolicyMapper.insertPolicy(policy);
        List<ReadPolicyEntity> readPolicies = readPolicyMapper.findAll();
        model.addAttribute("readPolicies", readPolicies);
	    return "redirect:/admin/menu/readPolicy";
	}

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
			if(policyDetails.getContent1() != null) {
				policyDetails.setContent1(policyService.decodingContent(policyDetails.getContent1()));
	        }
	        if(policyDetails.getContent2() != null) {
	        	policyDetails.setContent2(policyService.decodingContent(policyDetails.getContent2()));
	        }
	        if(policyDetails.getContent3() != null) {
	        	policyDetails.setContent3(policyService.decodingContent(policyDetails.getContent3()));
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		model.addAttribute("policyDetails", policyDetails);
		return "updatePolicy";
	}
	
	@PostMapping("/admin/menu/readPolicy/updatePolicy")
	public String updatePolicy(@RequestParam("id") int detected_no, UpdatePolicyEntity policy, Model model) {
		try {
			if ("any".equalsIgnoreCase(policy.getSrc_ip().trim())) {
				policy.setSrc_ip("0");
			}
			if ("any".equalsIgnoreCase(policy.getSrc_port().trim())) {
				policy.setSrc_port("0");
			}
			if(policy.getContent1() != null) {
	            policy.setContent1(policyService.encodingContent(policy.getContent1()));
	        }
	        if(policy.getContent2() != null) {
	            policy.setContent2(policyService.encodingContent(policy.getContent2()));
	        }
	        if(policy.getContent3() != null) {
	            policy.setContent3(policyService.encodingContent(policy.getContent3()));
	        }
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
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
			if(viewPolicy.getContent1() != null) {
				viewPolicy.setContent1(policyService.decodingContent(viewPolicy.getContent1()));
	        }
	        if(viewPolicy.getContent2() != null) {
	        	viewPolicy.setContent2(policyService.decodingContent(viewPolicy.getContent2()));
	        }
	        if(viewPolicy.getContent3() != null) {
	        	viewPolicy.setContent3(policyService.decodingContent(viewPolicy.getContent3()));
	        }
	    } catch (UnsupportedEncodingException e) {
	    	e.printStackTrace();
	    }
	    model.addAttribute("viewPolicy", viewPolicy);
	    return "viewPolicy";
	}
}