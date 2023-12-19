package com.example.demo.controller;

import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
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
public class HomeController {
	
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
	public List<ReadLogsEntity> getLastLogs(@RequestParam("date") String dateStr) {
	    LocalDate date = LocalDate.parse(dateStr);
	    String tableName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	    
	    List<ReadLogsEntity> logs = new ArrayList<>();
	    try {
	        logs.addAll(readLogsMapper.getLastLogs(tableName, date.atStartOfDay(), date.atTime(23, 59, 59)));
	    } catch (DataAccessException e) {
	        if (e.getRootCause() instanceof SQLSyntaxErrorException) {
	            System.out.println("========== <" + tableName + "> 해당 로그 테이블이 존재하지 않습니다. ==========");
	        } else {
	            e.printStackTrace();
	        }
	    }
	    return logs;
	}
	
	@GetMapping("/admin/menu/home/api/logWeek")
	@ResponseBody
	public List<ReadLogsEntity> getWeekLogs(@RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr) {
	    LocalDate startDate = LocalDate.parse(startDateStr);
	    LocalDate endDate = LocalDate.parse(endDateStr);
	    List<ReadLogsEntity> logs = new ArrayList<>();
	    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
	        String tableName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        try {
	            logs.addAll(readLogsMapper.getWeekLogs(tableName, date.atStartOfDay(), date.atTime(23, 59, 59)));
	        } catch (DataAccessException e) {
	            if (e.getRootCause() instanceof SQLSyntaxErrorException) {
	                System.out.println("========== <" + tableName + "> 해당 로그 테이블이 존재하지 않습니다. ==========");
	            } else {
	                e.printStackTrace();
	            }
	        }
	    }
	    return logs;
	}
}