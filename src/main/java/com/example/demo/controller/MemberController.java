package com.example.demo.controller;

import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.user.MemberEntity;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    
    @GetMapping("/")
    public String showLoginForm_1() {
    	return "redirect:/admin";
    }
    
    @GetMapping("/admin")
    public String showLoginForm_2() {
        return "login";
    }
    
    @GetMapping("/admin/")
    public String showLoginForm_3() {
    	return "redirect:/admin";
    }

    @PostMapping("/admin")
    public String login(@ModelAttribute MemberEntity memberEntity, Model model, HttpSession session) throws NoSuchAlgorithmException {
        if (memberService.login(memberEntity)) {
        	session.setAttribute("id", memberEntity);
        	session.setMaxInactiveInterval(30 * 60);
            return "redirect:/admin/menu";
        } else {
            return "redirect:/admin";
        }
    }
    
	@PostMapping("/admin/menu/changePassword")
	public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword) throws NoSuchAlgorithmException {
		boolean isChanged = memberService.changePassword(currentPassword, newPassword);
		if (isChanged) {
			return "redirect:/admin/menu/setup?changePassword=true";
		} else {
			return "redirect:/admin/menu/setup?changePassword=false";
		}
	}
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin";
    }
    
    @GetMapping("/admin/menu")
    public String showMenuForm(MemberEntity memberEntity, HttpSession session) {
    	memberEntity = (MemberEntity)session.getAttribute("id");
    	if (memberEntity != null) {
    		return "menu";
    	} else {
    		return "redirect:/admin";
    	}
    }
    
    @GetMapping("/admin/home")
    public String showHomeForm() {
    	return "home";
    }
}
