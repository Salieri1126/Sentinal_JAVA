package com.example.demo.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.log.ReadLogsEntity;
import com.example.demo.repository.log.ReadLogsMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogController {
	
	private final ReadLogsMapper readLogMapper;
	
	@GetMapping("/admin/menu/readLogs")
	public String showLogListForm(
			Model model,
			@RequestParam(name = "ip", required = false) String ip,
			@RequestParam(name = "port", required = false) Integer port,
	        @RequestParam(name = "level", required = false, defaultValue = "-1") Integer level,
	        @RequestParam(name = "action", required = false, defaultValue = "-1") Integer action,
	        @RequestParam(name = "startDate", required = false) String startDateStr,
	        @RequestParam(name = "startTime", required = false) String startTimeStr,
	        @RequestParam(name = "endDate", required = false) String endDateStr,
	        @RequestParam(name = "endTime", required = false) String endTimeStr) {

	    if (ip != null && ip.isEmpty()) ip = null;
	    if (level == -1) level = null;
	    if (action == -1) action = null;
	    
	    LocalDateTime startDate = null;
	    if (startDateStr != null && !startDateStr.isEmpty() && startTimeStr != null && !startTimeStr.isEmpty()) {
	        startDate = LocalDateTime.parse(startDateStr + " " + startTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    }
	    LocalDateTime endDate = null;
	    if (endDateStr != null && !endDateStr.isEmpty() && endTimeStr != null && !endTimeStr.isEmpty()) {
	        endDate = LocalDateTime.parse(endDateStr + " " + endTimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    }

	    List<ReadLogsEntity> readLogs = readLogMapper.findAll();

	    if (ip != null || port != null || level != null || action != null || startDate != null || endDate != null) {
	        readLogs = readLogMapper.findFilteredLogs(ip, port, level, action, startDate, endDate);
	    }

	    readLogs.forEach(
	            log -> log.setTimeFormatted(
	                    log.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

	    model.addAttribute("readLogs", readLogs);
	    return "readLogs";
	}
}
