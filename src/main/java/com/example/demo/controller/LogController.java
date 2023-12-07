package com.example.demo.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
            @RequestParam(name = "level", required = false, defaultValue = "-1") Integer level,
            @RequestParam(name = "action", required = false, defaultValue = "-1") Integer action,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "startTime", required = false) String startTimeStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            @RequestParam(name = "endTime", required = false) String endTimeStr) {
    	
    	if ("".equalsIgnoreCase(ip)) ip = null;
    	if ("any".equalsIgnoreCase(ip)) ip = null;
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
        
        List<ReadLogsEntity> readLogs = new ArrayList<>();
        
        if (startDateStr != null && !startDateStr.isEmpty() && endDateStr != null && !endDateStr.isEmpty()) {
            LocalDate start = startDate.toLocalDate();
            LocalDate end = endDate.toLocalDate();
            for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                String tableName = "log_" + date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                
                if (ip != null || level != null || action != null) {
                    readLogs.addAll(readLogMapper.findFilteredLogs(tableName, ip, level, action, startDate, endDate));
                } else {
                    readLogs.addAll(readLogMapper.findAll(tableName));
                }
            }
        } else {
            String tableName = "log_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            readLogs = readLogMapper.findAll(tableName);
        }

        readLogs.forEach(
                log -> log.setTimeFormatted(
                        log.getTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));

        model.addAttribute("readLogs", readLogs);
        model.addAttribute("ip", ip);
        model.addAttribute("level", level);
        model.addAttribute("action", action);
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("startTime", startTimeStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("endTime", endTimeStr);
        model.addAttribute("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return "readLogs";
    }
}