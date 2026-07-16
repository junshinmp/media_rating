package com.db_connector.rating;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class UnifiedTestCLI implements CommandLineRunner {

    private final RestClient localClient;
    private final String baseUrl = "http://localhost:8080";

    // Track statistics
    private int totalTestsRun = 0;
    private int passedTests = 0;
    private int failedTests = 0;
    private final List<String> failuresList = new ArrayList<>();

    // Variables to dynamically pass state between tests
    private Integer createdUserId = null;
    private Integer createdRatingId = null;
    private final String testUsername = "automation_tester_77";
    private final String testMovieId = "27205"; // Inception (Valid TMDB ID)

    public UnifiedTestCLI() {
        this.localClient = RestClient.create();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========================================================");
        System.out.println("🤖 STARTING AUTOMATED END-TO-END CONTROLLER TEST SUITE 🤖");
        System.out.println("========================================================");

        // --- PRE-SUITE CLEANUP ---
        // Ensure the test user from previous runs doesn't exist so creations don't fail immediately
        silentCleanup();

        // --- PHASE 1: USER CONTROLLER TESTS ---
        runTest(1, "POST /createUser - Register User", () -> {
            String userJson = String.format(
                "{\"username\":\"%s\",\"firstName\":\"Auto\",\"lastName\":\"Tester\",\"password\":\"Password123\"}",
                testUsername
            );
            String response = localClient.post()
                    .uri(baseUrl + "/createUser")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userJson)
                    .retrieve()
                    .body(String.class);
            
            // Extract the generated ID from the returned JSON user object
            if (response != null && response.contains("\"id\":")) {
                String idStr = response.split("\"id\":")[1].split(",")[0].trim();
                createdUserId = Integer.parseInt(idStr);
            }
            return response;
        });

        runTest(2, "POST /createUser - Check Duplicate User Prevention (Expect Fail)", () -> {
            String userJson = String.format(
                "{\"username\":\"%s\",\"firstName\":\"Auto\",\"lastName\":\"Tester\",\"password\":\"Password123\"}",
                testUsername
            );
            try {
                localClient.post()
                        .uri(baseUrl + "/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(userJson)
                        .retrieve()
                        .body(String.class);
                throw new RuntimeException("Validation failed: Allowed duplicate user registration!");
            } catch (HttpClientErrorException.BadRequest e) {
                return "Successfully caught expected error: " + e.getResponseBodyAsString();
            }
        });

        runTest(3, "GET /findUsers - Find User by Username", () -> {
            return localClient.get()
                    .uri(baseUrl + "/findUsers?username={username}", testUsername)
                    .retrieve()
                    .body(String.class);
        });

        runTest(4, "GET /userById - Find User by Database ID", () -> {
            if (createdUserId == null) throw new IllegalStateException("Dependency missing: No User ID registered");
            return localClient.get()
                    .uri(baseUrl + "/userById?userId={userId}", createdUserId)
                    .retrieve()
                    .body(String.class);
        });

        runTest(5, "GET /checkUsername - Verify Username Existence Flag", () -> {
            return localClient.get()
                    .uri(baseUrl + "/checkUsername?username={username}", testUsername)
                    .retrieve()
                    .body(String.class);
        });

        runTest(6, "GET /allUsers - Retrieve All Users List", () -> {
            return localClient.get()
                    .uri(baseUrl + "/allUsers")
                    .retrieve()
                    .body(String.class);
        });

        runTest(7, "PUT /changePassword - Update User Password", () -> {
            if (createdUserId == null) throw new IllegalStateException("Dependency missing: No User ID registered");
            return localClient.put()
                    .uri(baseUrl + "/changePassword?userID={userID}&currentPass={currentPass}&newPass={newPass}", 
                         createdUserId, "Password123", "NewSecurePassword456")
                    .retrieve()
                    .body(String.class);
        });

        runTest(8, "POST /login - Authenticate with New Password", () -> {
            String loginJson = String.format(
                "{\"username\":\"%s\",\"password\":\"NewSecurePassword456\"}",
                testUsername
            );
            return localClient.post()
                    .uri(baseUrl + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(loginJson)
                    .retrieve()
                    .body(String.class);
        });

        // --- PHASE 2: TMDB CONTROLLER TESTS ---
        runTest(9, "GET /search - Query TMDB API for Media", () -> {
            return localClient.get()
                    .uri(baseUrl + "/search?title={title}", "Inception")
                    .retrieve()
                    .body(String.class);
        });

        runTest(10, "GET /movieFromId - Fetch TMDB Details by ID", () -> {
            return localClient.get()
                    .uri(baseUrl + "/movieFromId?id={id}", testMovieId)
                    .retrieve()
                    .body(String.class);
        });

        // --- PHASE 3: RATING CONTROLLER TESTS ---
        runTest(11, "POST /createRating - Create New Movie Rating", () -> {
            if (createdUserId == null) throw new IllegalStateException("Dependency missing: No User ID registered");
            String ratingJson = String.format(
                "{\"userId\":%d,\"mediaId\":\"%s\",\"stars\":5,\"comments\":\"Incredible sci-fi masterpiece!\"}",
                createdUserId, testMovieId
            );
            String response = localClient.post()
                    .uri(baseUrl + "/createRating")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ratingJson)
                    .retrieve()
                    .body(String.class);

            if (response != null && response.contains("\"id\":")) {
                String idStr = response.split("\"id\":")[1].split(",")[0].trim();
                createdRatingId = Integer.parseInt(idStr);
            }
            return response;
        });

        runTest(12, "POST /createRating - Duplicate Rating Prevention (Expect Fail)", () -> {
            if (createdUserId == null) throw new IllegalStateException("Dependency missing: No User ID registered");
            String ratingJson = String.format(
                "{\"userId\":%d,\"mediaId\":\"%s\",\"stars\":3,\"comments\":\"Duplicate test\"}",
                createdUserId, testMovieId
            );
            try {
                localClient.post()
                        .uri(baseUrl + "/createRating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ratingJson)
                        .retrieve()
                        .body(String.class);
                throw new RuntimeException("Validation failed: Allowed duplicate movie rating by same user!");
            } catch (HttpClientErrorException.BadRequest e) {
                return "Successfully caught expected error: " + e.getResponseBodyAsString();
            }
        });

        runTest(13, "POST /createRating - Check Star Bounds Validation (Expect Fail)", () -> {
            if (createdUserId == null) throw new IllegalStateException("Dependency missing: No User ID registered");
            String ratingJson = String.format(
                "{\"userId\":%d,\"mediaId\":\"%s\",\"stars\":99,\"comments\":\"Over limits test\"}",
                createdUserId, testMovieId
            );
            try {
                localClient.post()
                        .uri(baseUrl + "/createRating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ratingJson)
                        .retrieve()
                        .body(String.class);
                throw new RuntimeException("Validation failed: Allowed stars score outside 1-5 range!");
            } catch (HttpClientErrorException.BadRequest e) {
                return "Successfully caught expected error: " + e.getResponseBodyAsString();
            }
        });

        runTest(14, "GET /ratingId - Find Rating by Database ID", () -> {
            if (createdRatingId == null) throw new IllegalStateException("Dependency missing: No Rating ID registered");
            return localClient.get()
                    .uri(baseUrl + "/ratingId?ratingId={ratingId}", createdRatingId)
                    .retrieve()
                    .body(String.class);
        });

        runTest(15, "GET /ratingUserId - Get Ratings Associated with User ID", () -> {
            if (createdUserId == null) throw new IllegalStateException("Dependency missing: No User ID registered");
            return localClient.get()
                    .uri(baseUrl + "/ratingUserId?userId={userId}", createdUserId)
                    .retrieve()
                    .body(String.class);
        });

        runTest(16, "GET /ratingsMediaId - Get Ratings Associated with Media ID", () -> {
            return localClient.get()
                    .uri(baseUrl + "/ratingsMediaId?mediaId={mediaId}", testMovieId)
                    .retrieve()
                    .body(String.class);
        });

        runTest(17, "GET /ratingUserAndMedia - Get Specific User Rating for Specific Media", () -> {
            if (createdUserId == null) throw new IllegalStateException("Dependency missing: No User ID registered");
            return localClient.get()
                    .uri(baseUrl + "/ratingUserAndMedia?mediaId={mediaId}&userId={userId}", testMovieId, createdUserId)
                    .retrieve()
                    .body(String.class);
        });

        runTest(18, "GET /averageRating - Calculate Aggregate Media Rating Score", () -> {
            return localClient.get()
                    .uri(baseUrl + "/averageRating?mediaId={mediaId}", testMovieId)
                    .retrieve()
                    .body(String.class);
        });

        runTest(19, "PUT /changeRating - Modify Existing Stars and Comments", () -> {
            if (createdRatingId == null) throw new IllegalStateException("Dependency missing: No Rating ID registered");
            return localClient.put()
                    .uri(baseUrl + "/changeRating?ratingId={ratingId}&stars={stars}&comments={comments}",
                         createdRatingId, 4, "Actually, it is a 4-star movie on second view.")
                    .retrieve()
                    .body(String.class);
        });

        runTest(20, "DELETE /deleteRating - Delete Single Movie Rating", () -> {
            if (createdRatingId == null) throw new IllegalStateException("Dependency missing: No Rating ID registered");
            return localClient.delete()
                    .uri(baseUrl + "/deleteRating?ratingId={ratingId}", createdRatingId)
                    .retrieve()
                    .body(String.class);
        });

        runTest(21, "DELETE /deleteUser - Purge Created Test User", () -> {
            return localClient.delete()
                    .uri(baseUrl + "/deleteUser?username={username}", testUsername)
                    .retrieve()
                    .body(String.class);
        });

        // --- PHASE 4: DIAGNOSTIC REPORT ---
        printDiagnosticReport();
    }

    /**
     * Test execution wrapper. Counts and numbers each test automatically.
     */
    private void runTest(int testNum, String testName, ApiCallRunnable testBody) {
        totalTestsRun++;
        System.out.printf("\n[Test %02d] Running: %s...\n", testNum, testName);
        try {
            long startTime = System.currentTimeMillis();
            String response = testBody.run();
            long duration = System.currentTimeMillis() - startTime;

            passedTests++;
            System.out.println("  🟢 PASSED (" + duration + "ms)");
            System.out.println("  Response: " + truncate(response, 120));
        } catch (Exception e) {
            failedTests++;
            String errorMessage = String.format("Test %02d (%s) failed with error: %s", testNum, testName, e.getMessage());
            failuresList.add(errorMessage);
            System.err.println("  🔴 FAILED");
            System.err.println("  Reason: " + e.getMessage());
        }
    }

    /**
     * Silent cleanup utility to remove old records without skewing success stats.
     */
    private void silentCleanup() {
        try {
            localClient.delete()
                    .uri(baseUrl + "/deleteUser?username={username}", testUsername)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ignored) {
            // Ignored as user might not exist in db initially
        }
    }

    private void printDiagnosticReport() {
        System.out.println("\n========================================================");
        System.out.println("📊 AUTOMATED TEST SUITE EXECUTION REPORT");
        System.out.println("========================================================");
        System.out.println("  Total Executed Tests : " + totalTestsRun);
        System.out.println("  Passed Tests         : 🟢 " + passedTests);
        System.out.println("  Failed Tests         : 🔴 " + failedTests);
        System.out.println("========================================================");

        if (failedTests > 0) {
            System.err.println("\n🔴 SPECIFIC FAILURES IDENTIFIED:");
            for (String failure : failuresList) {
                System.err.println("  - " + failure);
            }
            System.err.println("\nAction Recommended: Check the console output above or confirm database/network settings.");
        } else {
            System.out.println("\n🟢 SUCCESS! All endpoints integrated seamlessly with zero issues.");
        }
        System.out.println("========================================================\n");
    }

    private String truncate(String text, int length) {
        if (text == null) return "null";
        return text.length() <= length ? text : text.substring(0, length) + "...";
    }

    @FunctionalInterface
    private interface ApiCallRunnable {
        String run() throws Exception;
    }
}