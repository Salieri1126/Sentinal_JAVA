package com.example.demo.controller;

import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.log.ReadLogsEntity;
import com.example.demo.repository.log.ReadLogsMapper;
import com.example.demo.service.LogService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LogController {
    
    private final ReadLogsMapper readLogsMapper;
    private final LogService logService;

    @GetMapping("/admin/menu/readLogs")
    public String showLogListForm(Model model) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("today", today);
        model.addAttribute("startDate", today);
        model.addAttribute("endDate", today);
        return "readLogs";
    }
    
    @GetMapping("/admin/menu/readLogs/api/getLogList")
    @ResponseBody
    public List<ReadLogsEntity> getLogList(
            @RequestParam(name = "src_ip", required = false) String src_ip,
            @RequestParam(name = "src_port", required = false) String src_port,
            @RequestParam(name = "dst_ip", required = false) String dst_ip,
            @RequestParam(name = "detected_name", required = false) String detected_name,
            @RequestParam(name = "level", required = false, defaultValue = "-1") Integer level,
            @RequestParam(name = "action", required = false, defaultValue = "-1") Integer action,
            @RequestParam(name = "startDate", required = false) String startDateStr,
            @RequestParam(name = "startTime", required = false) String startTimeStr,
            @RequestParam(name = "endDate", required = false) String endDateStr,
            @RequestParam(name = "endTime", required = false) String endTimeStr,
            @RequestParam(name = "page", required = true) Integer page,
            @RequestParam(name = "numPerPage", required = true) Integer numPerPage) {

        final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        
        detected_name = logService.validateParam(detected_name);
        src_ip = logService.validateParam(src_ip);
        src_port = logService.validateParam(src_port);
        dst_ip = logService.validateParam(dst_ip);
        level = logService.validateParam(level, -1);
        action = logService.validateParam(action, -1);
        
        LocalDateTime startDate = null;
        if (startDateStr != null && !startDateStr.isEmpty() && startTimeStr != null && !startTimeStr.isEmpty()) {
            startDate = LocalDateTime.parse(startDateStr + " " + startTimeStr, formatter);
        }
        LocalDateTime endDate = null;
        if (endDateStr != null && !endDateStr.isEmpty() && endTimeStr != null && !endTimeStr.isEmpty()) {
            endDate = LocalDateTime.parse(endDateStr + " " + endTimeStr, formatter);
        } else {
            endDate = LocalDateTime.now();
        }

        List<ReadLogsEntity> allLogs = new ArrayList<>();
        LocalDate tempDate = startDate.toLocalDate();
        while (!tempDate.isAfter(endDate.toLocalDate())) {
            String tableName = "log_" + tempDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            try {
                allLogs.addAll(readLogsMapper.findFilteredLogs(tableName, src_ip, src_port, dst_ip, detected_name, level, action, startDate, endDate));
            } catch (DataAccessException e) {
                if (e.getRootCause() instanceof SQLSyntaxErrorException) {
                    System.out.println("========== <" + tableName + "> 해당 로그 테이블이 존재하지 않습니다. ==========");
                } else {
                    System.out.println("========== <" + tableName + "> 처리 중 예외가 발생했습니다. ==========");
                    e.printStackTrace();
                }
            }
            tempDate = tempDate.plusDays(1);
        }
        
        List<ReadLogsEntity> readLogs = allLogs.stream()
            .sorted(Comparator.comparing(ReadLogsEntity::getTime).thenComparing(ReadLogsEntity::getLog_index))
            .skip((long)(page-1) * numPerPage)
            .limit(numPerPage)
            .collect(Collectors.toList());
        readLogs.forEach(ReadLogsEntity::setLogdateFromTime);
        return readLogs;
    }
    
    @GetMapping("/admin/menu/readLogs/viewLogs/{log_date}/{log_index}")
    public String showBinaryData(@PathVariable String log_date, @PathVariable int log_index, Model model) {
        LocalDateTime dateTime = LocalDateTime.parse(log_date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String tableName = "log_" + formattedDate;
        ReadLogsEntity log = readLogsMapper.getBinaryData(tableName, log_index);
        String binaryData = logService.getPacket(log);
        List<Map<String, String>> packetHeader = logService.getHeader(log);
        model.addAttribute("binaryData", binaryData);
        model.addAttribute("packetHeader", packetHeader);
        return "viewLogs";
    }
}
