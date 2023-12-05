package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.log.ReadLogEntity;
import com.example.demo.repository.log.ReadLogMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogController {
	
	private final ReadLogMapper readLogMapper;
	
	@GetMapping("/admin/menu/readLogs")
	public String showLogListForm(
			Model model,
			@RequestParam(name = "ip", required = false) String ip,
			@RequestParam(name = "port", required = false) Integer port,
			@RequestParam(name = "levelApply", required = false) Boolean levelApply,
			@RequestParam(name = "level", required = false) Integer level,
			@RequestParam(name = "actionApply", required = false) Boolean actionApply,
			@RequestParam(name = "action", required = false) Integer action,
			@RequestParam(name = "startDate", required = false) String startDateStr,
			@RequestParam(name = "endDate", required = false) String endDateStr) {
		
		if (ip != null && ip.isEmpty()) ip = null;
		if (levelApply != null && !levelApply) level = null;
		if (actionApply != null && !actionApply) action = null;
		
		LocalDateTime startDate = null;
		if (startDateStr != null && !startDateStr.isEmpty()) {
			startDate = LocalDateTime.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		LocalDateTime endDate = null;
		if (endDateStr != null && !endDateStr.isEmpty()) {
			endDate = LocalDateTime.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		}
		
		List<ReadLogEntity> readLogs = readLogMapper.findFilteredLogs(ip, port, level, action, startDate, endDate);
		
		readLogs.forEach(
				log -> log.setTimeFormatted(
						log.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
		
		model.addAttribute("readLogs", readLogs);
		return "readLogs";
	}
}
