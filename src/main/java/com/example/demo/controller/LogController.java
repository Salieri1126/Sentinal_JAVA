package com.example.demo.controller;

import java.sql.SQLSyntaxErrorException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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

/**
 * 로그 컨트롤러 클래스
 */
@Controller
@RequiredArgsConstructor
public class LogController {

    private final ReadLogsMapper readLogsMapper;
    private final LogService logService;

    /**
     * 로그 목록 화면을 보여주는 메서드
     *
     * @param model Model 객체
     * @return 로그 목록 템플릿 이름
     */
    @GetMapping("/admin/menu/readLogs")
    public String showLogListForm(Model model) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        model.addAttribute("today", today);
        model.addAttribute("startDate", today);
        model.addAttribute("endDate", today);
        return "readLogs";
    }

    /**
     * 필터링된 로그 목록을 가져오는 메서드
     *
     * @param src_ip         소스 IP 주소
     * @param src_port       소스 포트
     * @param dst_ip         대상 IP 주소
     * @param detected_name  감지된 이름
     * @param level          로그 레벨
     * @param action         로그 액션
     * @param startDateStr   조회 시작 날짜 문자열
     * @param startTimeStr   조회 시작 시간 문자열
     * @param endDateStr     조회 종료 날짜 문자열
     * @param endTimeStr     조회 종료 시간 문자열
     * @param page           페이지 번호
     * @param numPerPage     페이지당 로그 수
     * @return 필터링된 로그 리스트와 로그의 개수를 담은 Map
     */
    @GetMapping("/admin/menu/readLogs/api/getLogList")
    @ResponseBody
    public Map<String, Object> getLogList(
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
                .skip((long) (page - 1) * numPerPage)
                .limit(numPerPage)
                .collect(Collectors.toList());
        readLogs.forEach(ReadLogsEntity::setLogdateFromTime);
        
        int totalLogCount = allLogs.size();

        Map<String, Object> result = new HashMap<>();
        result.put("readLogs", readLogs);
        result.put("totalLogCount", totalLogCount);
        return result;
    }

    /**
     * 상세 로그를 보여주는 메서드
     *
     * @param log_date  로그 날짜
     * @param log_index 로그 인덱스
     * @param model     Model 객체
     * @return 바이너리 데이터 템플릿 이름
     */
    @GetMapping("/admin/menu/readLogs/viewLogs/{log_date}/{log_index}")
    public String showBinaryData(@PathVariable String log_date, @PathVariable int log_index, Model model) {
        LocalDateTime dateTime = LocalDateTime.parse(log_date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String tableName = "log_" + formattedDate;
        ReadLogsEntity log = readLogsMapper.getBinaryData(tableName, log_index);
        String binaryData = logService.getPacket(log);
        String asciiData = logService.toASCII(binaryData);
        List<Map<String, String>> packetHeader = logService.getHeader(log);
        model.addAttribute("binaryData", binaryData);
        model.addAttribute("asciiData", asciiData);
        model.addAttribute("packetHeader", packetHeader);
        return "viewLogs";
    }
}