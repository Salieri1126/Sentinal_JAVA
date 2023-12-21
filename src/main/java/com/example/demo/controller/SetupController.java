package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.file.SetupEntity;
import com.example.demo.service.SetupService;

import lombok.RequiredArgsConstructor;

/**
 * 설정 컨트롤러 클래스
 */
@Controller
@RequiredArgsConstructor
public class SetupController {

    private final SetupService fileService;

    /**
     * 설정 화면을 보여주는 메서드
     *
     * @return 설정 템플릿 이름
     */
    @GetMapping("/admin/menu/setup")
    public String showSetupForm() {
        return "setup";
    }

    /**
     * 설정을 업데이트하는 메서드
     *
     * @param confFileEntity SetupEntity 객체
     * @return 관리자 메뉴 페이지로의 리다이렉트
     */
    @PostMapping("/admin/menu/setup")
    public String updateFile(@ModelAttribute SetupEntity confFileEntity) {
        fileService.makeConfFile(confFileEntity);
        return "redirect:/admin/menu";
    }
}
