package com.example.demo.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.demo.model.file.SetupEntity;

@Service
public class SetupService {
	
	public void makeConfFile(SetupEntity setupEntity) {
		final String PATH = "/home/rocky/manager/dbms_ips_manager.conf";
        final File FILE = new File(PATH);
        final File BACKUP_FILE = new File(PATH + "_backup");

        if (FILE.exists()) {
        	FILE.renameTo(BACKUP_FILE);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH))) {
            writer.write("SNIFF_NIC = " + setupEntity.getDbNIC() + "\n\n");
            writer.write("DB_IP = " + setupEntity.getDbIP() + "\n");
            writer.write("DB_PORT = " + setupEntity.getDbPORT() + "");
            
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
