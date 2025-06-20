package com.hustict.aims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startFrontendAndOpenBrowser(ApplicationReadyEvent event) {
        File currentWorkingDir = new File(System.getProperty("user.dir"));
        
        File frontendDir = new File(currentWorkingDir, "frontend");
        
        if (!frontendDir.exists() || !frontendDir.isDirectory()) {
            System.err.println("Error: Frontend directory not found at " + frontendDir.getAbsolutePath());
            System.err.println("Expected path: " + frontendDir.getAbsolutePath());
            System.err.println("Current working directory: " + currentWorkingDir.getAbsolutePath());
            return;
        }

        System.out.println("Frontend directory found: " + frontendDir.getAbsolutePath());
        
        File packageJsonFile = new File(frontendDir, "package.json");
        if (!packageJsonFile.exists()) {
            System.err.println("Error: package.json not found in " + frontendDir.getAbsolutePath());
            return;
        }

        String npmCommand = isWindows() ? "npm.cmd" : "npm";

        new Thread(() -> {
            try {
                System.out.println("Starting React frontend on port 3000...");

                ProcessBuilder pb = new ProcessBuilder(npmCommand, "start");
                pb.directory(frontendDir);
                pb.inheritIO();

                Process process = pb.start();
                
                System.out.println("Frontend server started successfully.");
                
                new Thread(() -> {
                    try {
                        int exitCode = process.waitFor();
                        System.out.println("Frontend server stopped with exit code: " + exitCode);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Frontend process monitoring was interrupted.");
                    }
                }).start();

            } catch (IOException e) {
                System.err.println("Failed to start frontend server: " + e.getMessage());
                e.printStackTrace();
            }
        }, "Frontend-Starter-Thread").start();

        try {
            System.out.println("Waiting for frontend to start...");
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for frontend to start.");
        }

        openBrowser("http://localhost:3000");
    }
    
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
    
    private void openBrowser(String url) {
        try {
            System.out.println("Attempting to open browser to: " + url);
            
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            
            if (os.contains("win")) {
                pb = new ProcessBuilder("cmd.exe", "/c", "start", url);
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", url);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
                pb = new ProcessBuilder("xdg-open", url);
            } else {
                System.out.println("Auto-open browser not supported on this OS. Please access manually: " + url);
                return;
            }
            
            pb.start();
            System.out.println("Browser opened using system command.");
        } catch (Exception e) {
            System.err.println("Failed to open browser: " + e.getMessage());
            System.out.println("Please access the application at: " + url);
        }
    }
}