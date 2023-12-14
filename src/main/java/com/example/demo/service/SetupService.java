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
		final String path = "/home/rocky/manager/dbms_ips_manager.conf";
        final File file = new File(path);
        final File backupFile = new File(path + "_backup");

        if (file.exists()) {
            file.renameTo(backupFile);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("SNIFF_NIC = " + setupEntity.getDbNIC() + "\n\n");
            writer.write("DB_IP = " + setupEntity.getDbIP() + "\n");
            writer.write("DB_PORT = " + setupEntity.getDbPORT() + "");
            
            try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
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
                System.err.println("Read Backup .Conf File Error: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Make .Conf File Error: " + e.getMessage());
        }
	}
}
