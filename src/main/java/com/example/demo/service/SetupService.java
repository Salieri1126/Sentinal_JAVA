package com.example.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.demo.model.file.SetupEntity;

/**
 * 설정 파일을 생성하고 관리하는 서비스 클래스
 */
@Service
public class SetupService {

    /**
     * 설정 엔터티를 이용하여 환경 설정 파일을 생성하는 메서드
     *
     * @param setupEntity 환경 설정을 담고 있는 엔터티
     */
    public void makeConfFile(SetupEntity setupEntity) {
        final String PATH = "/home/rocky/manager/dbms_ips_manager.conf";
        final File FILE = new File(PATH);
        final File BACKUP_FILE = new File(PATH + "_backup");

        // 기존 파일이 존재하는 경우 백업 파일로 변경합니다.
        if (FILE.exists()) {
            FILE.renameTo(BACKUP_FILE);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH))) {
            // 새로운 설정을 파일에 씁니다.
            writer.write("SNIFF_NIC = " + setupEntity.getDbNIC() + "\n\n");
            writer.write("DB_IP = " + setupEntity.getDbIP() + "\n");
            writer.write("DB_PORT = " + setupEntity.getDbPORT() + "");

            // 백업 파일에서 설정 파일로 이동할 때 다른 설정을 유지합니다.
            try (BufferedReader reader = new BufferedReader(new FileReader(BACKUP_FILE))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("SNIFF_NIC") ||
                            line.startsWith("DB_IP") ||
                            line.startsWith("DB_PORT")) {
                        continue;
                    }
                    writer.write(line + "\n");
                }
            } catch (IOException e) {
                System.err.println("백업 파일 읽어오는 중 에러 발생: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("파일 생성 중 에러 발생: " + e.getMessage());
        }
    }
}
