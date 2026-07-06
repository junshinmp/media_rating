package com.db_connector.rating;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.Scanner;

@Component
public class TMDBTestCLI implements CommandLineRunner {

    private final RestClient localClient;
    private final String baseUrl = "http://localhost:8080";

    // Build a simple local RestClient to hit your own endpoints
    public TMDBTestCLI() {
        this.localClient = RestClient.create();
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n===============================================");
        System.out.println("🎬 WELCOME TO THE TMDB CONTROLLER TESTER CLI 🎬");
        System.out.println("===============================================");

        while (running) {
            System.out.println("\nSelect an API route to test via Controller:");
            System.out.println("1) Test Search Endpoint (/api/v1/movies/search)");
            System.out.println("2) Test Find by IMDb ID Endpoint (/api/v1/movies/find)");
            System.out.println("3) Exit CLI");
            System.out.print("Enter choice (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("\nEnter media title search query (e.g., Inception): ");
                    String query = scanner.nextLine().trim();
                    if (!query.isEmpty()) {
                        // Updated path to "/search" and parameter key to "title"
                        executeApiCall(() -> localClient.get()
                                .uri(baseUrl + "/search?title={title}", query)
                                .retrieve()
                                .body(String.class));
                    }
                    break;

                case "2":
                    System.out.print("\nEnter IMDb ID string (e.g., tt1375666 for Inception): ");
                    String idInput = scanner.nextLine().trim();
                    
                    if (!idInput.isEmpty()) {
                        // Updated path to "/movieFromId" and parameter key to "id"
                        executeApiCall(() -> localClient.get()
                                .uri(baseUrl + "/movieFromId?id={id}", idInput)
                                .retrieve()
                                .body(String.class));
                    } else {
                        System.out.println("❌ Input cannot be empty.");
                    }
                    break;

                case "3":
                    System.out.println("\nExiting TMDB CLI tester... Application keeping server active.");
                    running = false;
                    break;

                default:
                    System.out.println("❌ Invalid selection. Choose options 1, 2, or 3.");
            }
        }
    }

    /**
     * Helper interface to handle API execution logging cleanly
     */
    private void executeApiCall(ApiCallRunnable call) {
        System.out.println("\n📡 Routing request through local Spring Boot Controller...");
        try {
            long startTime = System.currentTimeMillis();
            String jsonResponse = call.run();
            long duration = System.currentTimeMillis() - startTime;

            System.out.println("✅ Response Received via Controller in " + duration + "ms!");
            System.out.println("-------------------------------------------");
            System.out.println(jsonResponse);
            System.out.println("-------------------------------------------");
        } catch (Exception e) {
            System.err.println("❌ Controller Network Request Failed!");
            System.err.println("Error details: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface ApiCallRunnable {
        String run() throws Exception;
    }
}