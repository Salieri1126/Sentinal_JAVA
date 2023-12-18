package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("today", today);
        model.addAttribute("startDate", today);
        model.addAttribute("endDate", today);
	    return "home";
	}

	@GetMapping("/admin/menu/home/api/log")
	@ResponseBody
	public List<ReadLogsEntity> getTodayLogs() {
	    LocalDate today = LocalDate.now();
	    String tableName = "log_" + today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

	    List<ReadLogsEntity> logs = new ArrayList<>();
	    logs.addAll(readLogsMapper.getTodayLogs(tableName, today.atStartOfDay(), today.atTime(23, 59, 59)));
	    return logs;
	}
	
	@GetMapping("/admin/menu/home/api/logWeek")
	@ResponseBody
	public List<ReadLogsEntity> getWeekLogs() {
		LocalDate today = LocalDate.now();
	    LocalDate oneWeekAgo = today.minusDays(7);

	    List<ReadLogsEntity> logs = new ArrayList<>();
	    for (LocalDate date = oneWeekAgo; !date.isAfter(today); date = date.plusDays(1)) {
	        String tableName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        try {
	        	logs.addAll(readLogsMapper.getWeekLogs(tableName, date.atStartOfDay(), date.atTime(23, 59, 59)));
	    
	        } catch (DataAccessException e) {
	        	e.printStackTrace();
	        }
	    }
	    return logs;
	}
}
