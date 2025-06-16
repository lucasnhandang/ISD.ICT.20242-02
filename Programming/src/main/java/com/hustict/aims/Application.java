package com.hustict.aims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startFrontendAndOpenBrowser(ApplicationReadyEvent event) {
        File currentWorkingDir = new File(System.getProperty("user.dir"));
        File frontendDir = new File(currentWorkingDir, "Programming/frontend");

        System.out.println("Frontend directory: " + frontendDir.getAbsolutePath());

        if (!frontendDir.exists() || !frontendDir.isDirectory()) {
            System.err.println("Error: Frontend directory not found at " + frontendDir.getAbsolutePath());
            System.err.println("Check directory structure.");
            System.err.println("Adjust 'frontendDir' variable.");
            return;
        }

        new Thread(() -> {
            try {
                System.out.println("Starting React frontend on port 3000...");

                ProcessBuilder pb = new ProcessBuilder("npm", "start");
                pb.directory(frontendDir);
                pb.inheritIO();

                Process process = pb.start();

                int exitCode = process.waitFor();
                System.out.println("Frontend server stopped with exit code: " + exitCode);

            } catch (IOException | InterruptedException e) {
                System.err.println("Failed to start frontend server: " + e.getMessage());
                e.printStackTrace();
            }
        }, "Frontend-Starter-Thread").start();

        try {
            System.out.println("Waiting for frontend to start...");
            TimeUnit.SECONDS.sleep(15);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Interrupted while waiting for frontend.");
        }

        String frontendUrl = "http://localhost:3000";
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(frontendUrl));
                System.out.println("Browser opened to: " + frontendUrl);
            } else {
                System.out.println("Auto-open browser not supported. Access: " + frontendUrl);
            }
        } catch (Exception e) {
            System.err.println("Failed to open browser: " + e.getMessage());
            e.printStackTrace();
        }
    }
}