package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.log.ReadLogsEntity;
import com.example.demo.repository.log.ReadLogsMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeLogController {
	
	private final ReadLogsMapper readLogsMapper;
    
	@GetMapping("/admin/menu/home")
	public String showHomePage(Model model) {
	    LocalDate today = LocalDate.now();
	    LocalDate weekAgo = today.minusWeeks(1);
	    model.addAttribute("today", today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	    model.addAttribute("weekAgo", weekAgo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	    return "home";
	}

	@GetMapping("/admin/menu/home/api/log")
	@ResponseBody
	public List<ReadLogsEntity> getWeeklyLogs(
	        @RequestParam(name = "startDate", required = false) String startDateStr,
	        @RequestParam(name = "endDate", required = false) String endDateStr) {

	    LocalDate startDate = null;
	    if (startDateStr != null && !startDateStr.isEmpty()) {
	        startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    }
	    LocalDate endDate = null;
	    if (endDateStr != null && !endDateStr.isEmpty()) {
	        endDate = LocalDate.parse(endDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	    }

	    List<ReadLogsEntity> logs = new ArrayList<>();

	    if (startDate != null && endDate != null) {
	        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	            String tableName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	            logs.addAll(readLogsMapper.findFilteredLogs(tableName, null, null, null, startDate.atStartOfDay(), endDate.atTime(23, 59, 59)));
	        }
	    }

	    return logs;
	}




}
