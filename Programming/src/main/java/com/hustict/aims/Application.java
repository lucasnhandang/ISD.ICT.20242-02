package com.hustict.aims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void openBrowser() {
        try {
            // Wait for server to start
            Thread.sleep(2000);
            
            // Try to open browser using different commands based on OS
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;
            
            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "http://localhost:8080");
            } else if (os.contains("mac")) {
                processBuilder = new ProcessBuilder("open", "http://localhost:8080");
            } else {
                processBuilder = new ProcessBuilder("xdg-open", "http://localhost:8080");
            }
            
            processBuilder.start();
            
        } catch (Exception e) {
            System.out.println("Could not open browser: " + e.getMessage());
        }
    }
}
