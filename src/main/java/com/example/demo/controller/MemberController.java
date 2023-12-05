package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.user.MemberEntity;
import com.example.demo.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    
    @GetMapping("/admin")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/admin")
    public String login(@ModelAttribute MemberEntity memberEntity, Model model, HttpSession session) {
        if (memberService.login(memberEntity)) {
        	session.setAttribute("id", memberEntity);
        	session.setMaxInactiveInterval(30 * 60);
            return "redirect:/admin/menu";
        } else {
            return "redirect:/admin";
        }
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
}
