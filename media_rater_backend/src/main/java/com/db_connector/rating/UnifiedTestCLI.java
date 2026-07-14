package com.db_connector.rating;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UnifiedTestCLI implements CommandLineRunner {

    private final RestClient localClient;
    private final String baseUrl = "http://localhost:8080";

    public UnifiedTestCLI() {
        this.localClient = RestClient.create();
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n===============================================");
        System.out.println("🎬 WELCOME TO THE BACKEND CONTROLLER TESTER 👤");
        System.out.println("===============================================");

        while (running) {
            System.out.println("\nSelect a category to test:");
            System.out.println("--- 👤 USER ENDPOINTS ---");
            System.out.println("1) Create User (POST /createUser)");
            System.out.println("2) Find User by Username (GET /findUsers)");
            System.out.println("3) List All Users (GET /allUsers)");
            System.out.println("4) Change Password (PUT /changePassword)");
            System.out.println("5) Delete User (DELETE /deleteUser)");
            System.out.println("--- 🎬 TMDB MOVIE ENDPOINTS ---");
            System.out.println("6) Search Media Title (GET /search)");
            System.out.println("7) Find by IMDb ID (GET /movieFromId)");
            System.out.println("-------------------------");
            System.out.println("8) Exit CLI");
            System.out.print("Enter choice (1-8): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                // --- USER ENDPOINTS ---
                case "1":
                    System.out.print("\nEnter username: ");
                    String username = scanner.nextLine().trim();
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine().trim();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine().trim();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine().trim();

                    if (!username.isEmpty() && !password.isEmpty()) {
                        String userJson = String.format(
                            "{\"username\":\"%s\",\"firstName\":\"%s\",\"lastName\":\"%s\",\"password\":\"%s\"}",
                            username, firstName, lastName, password
                        );
                        executeApiCall(() -> localClient.post()
                                .uri(baseUrl + "/createUser")
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(userJson)
                                .retrieve()
                                .body(String.class));
                    } else {
                        System.out.println("❌ Username and Password cannot be empty.");
                    }
                    break;

                case "2":
                    System.out.print("\nEnter username to search: ");
                    String searchUser = scanner.nextLine().trim();
                    if (!searchUser.isEmpty()) {
                        executeApiCall(() -> localClient.get()
                                .uri(baseUrl + "/findUsers?username={username}", searchUser)
                                .retrieve()
                                .body(String.class));
                    }
                    break;

                case "3":
                    executeApiCall(() -> localClient.get()
                            .uri(baseUrl + "/allUsers")
                            .retrieve()
                            .body(String.class));
                    break;

                case "4":
                    System.out.print("\nEnter user ID: ");
                    String idInput = scanner.nextLine().trim();
                    System.out.print("Enter current password: ");
                    String currentPass = scanner.nextLine().trim();
                    System.out.print("Enter new password: ");
                    String newPass = scanner.nextLine().trim();

                    if (!idInput.isEmpty() && !currentPass.isEmpty() && !newPass.isEmpty()) {
                        executeApiCall(() -> localClient.put()
                                .uri(baseUrl + "/changePassword?userID={userID}&currentPass={currentPass}&newPass={newPass}", 
                                     idInput, currentPass, newPass)
                                .retrieve()
                                .body(String.class));
                    }
                    break;

                case "5":
                    System.out.print("\nEnter username to delete: ");
                    String deleteUser = scanner.nextLine().trim();
                    if (!deleteUser.isEmpty()) {
                        executeApiCall(() -> localClient.delete()
                                .uri(baseUrl + "/deleteUser?username={username}", deleteUser)
                                .retrieve()
                                .body(String.class));
                    }
                    break;

                // --- TMDB MOVIE ENDPOINTS ---
                case "6":
                    System.out.print("\nEnter media title search query (e.g., Inception): ");
                    String query = scanner.nextLine().trim();
                    if (!query.isEmpty()) {
                        executeApiCall(() -> localClient.get()
                                .uri(baseUrl + "/search?title={title}", query)
                                .retrieve()
                                .body(String.class));
                    }
                    break;

                case "7":
                    System.out.print("\nEnter IMDb ID string (e.g., 137106): ");
                    String idImg = scanner.nextLine().trim();
                    if (!idImg.isEmpty()) {
                        executeApiCall(() -> localClient.get()
                                .uri(baseUrl + "/movieFromId?id={id}", idImg)
                                .retrieve()
                                .body(String.class));
                    }
                    break;

                case "8":
                    System.out.println("\nExiting Unified CLI tester... Application keeping server active.");
                    running = false;
                    break;

                default:
                    System.out.println("❌ Invalid selection. Choose options 1 through 8.");
            }
        }
    }

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