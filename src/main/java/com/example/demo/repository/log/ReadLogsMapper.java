package com.example.demo.repository.log;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.log.ReadLogsEntity;

@Mapper
public interface ReadLogsMapper {
	List<ReadLogsEntity> findFilteredLogs(
			@Param("tableName") String tableName,
            @Param("ip") String ip,
            @Param("level") Integer level,
            @Param("action") Integer action,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
	ReadLogsEntity getBinaryData(
			@Param("tableName") String tableName,
			@Param("log_index") int log_index);
}
