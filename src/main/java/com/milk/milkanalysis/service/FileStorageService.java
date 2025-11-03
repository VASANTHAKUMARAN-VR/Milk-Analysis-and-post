package com.milk.milkanalysis.service;

import org.springframework.stereotype.Service;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import java.io.File;

@Service
public class FileStorageService {

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        try {
            // Multiple paths try pannum
            String[] possiblePaths = {
                    "uploads",
                    "/opt/render/project/src/uploads",
                    "/var/data/uploads",
                    System.getProperty("user.dir") + "/uploads"
            };

            for (String path : possiblePaths) {
                File uploadDir = new File(path);
                if (!uploadDir.exists()) {
                    boolean created = uploadDir.mkdirs();
                    System.out.println("📁 Created directory: " + path + " - " + (created ? "Success" : "Failed"));
                } else {
                    System.out.println("📁 Directory exists: " + path);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error creating upload directories: " + e.getMessage());
        }
    }
}