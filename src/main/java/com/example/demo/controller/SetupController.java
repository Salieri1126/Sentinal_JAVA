package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.file.ConfFileEntity;
import com.example.demo.service.FileService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SetupController {

    private final FileService fileService;

    @GetMapping("/admin/menu/setup")
    public String showSetupForm() {
        return "setup";
    }

    @PostMapping("/admin/menu/setup")
    public String updateFile(@ModelAttribute ConfFileEntity confFileEntity) {
        fileService.makeConfFile(confFileEntity);
        return "redirect:/admin/menu";
    }
}
