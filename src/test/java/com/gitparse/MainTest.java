package com.gitparse;

import com.gitparse.model.ParseResponse;
import com.gitparse.parse.GitService;
import io.javalin.Javalin;
import org.junit.jupiter.api.Test;
import io.javalin.testtools.JavalinTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testRootRoute() {
        // Arrange
        Javalin app = Main.createApp();

        JavalinTest.test(app, (server, client) -> {
            // Act
            var response = client.get("/");

            // Assert
            assertEquals(200, response.code());
            assertTrue(response.body().string().contains("John")); // Check if template rendered correctly
        });
    }

    @Test
    void testParseRepoRoute() {
        // Arrange
        String testRepoName = "test-repo";
        String testRepoStructure = "|--test-repo/";
        String testLastCommitTime = "2025-01-01 12:00:00";
        String testFileContents = "Sample file contents";

        GitService mockGitService = mock(GitService.class);
        ParseResponse mockResponse = new ParseResponse();
        mockResponse.setRepoStructure(testRepoStructure);
        mockResponse.setLastCommitTime(testLastCommitTime);
        mockResponse.setFileContents(testFileContents);

        when(mockGitService.clone(testRepoName)).thenReturn(mockResponse);

        Javalin app = Main.createApp(mockGitService);

        JavalinTest.test(app, (server, client) -> {
            // Act
            var response = client.get("/parse/" + testRepoName);

            // Assert
            assertEquals(200, response.code());
            var body = response.body().string();
            assertTrue(body.contains(testRepoStructure));
            assertTrue(body.contains(testLastCommitTime));
            assertTrue(body.contains(testFileContents));
        });
    }

    @Test
    void testApiParseRepoRoute() {
        // Arrange
        String testRepoName = "test-repo";
        String testRepoStructure = "|--test-repo/";
        String testLastCommitTime = "2025-01-01 12:00:00";
        String testFileContents = "Sample file contents";

        GitService mockGitService = mock(GitService.class);
        ParseResponse mockResponse = new ParseResponse();
        mockResponse.setRepoStructure(testRepoStructure);
        mockResponse.setLastCommitTime(testLastCommitTime);
        mockResponse.setFileContents(testFileContents);

        when(mockGitService.clone(testRepoName)).thenReturn(mockResponse);

        Javalin app = Main.createApp(mockGitService);

        JavalinTest.test(app, (server, client) -> {
            // Act
            var response = client.get("/api/v1/parse/" + testRepoName);

            // Assert
            assertEquals(200, response.code());
            var body = response.body().string();
            assertTrue(body.contains(testRepoStructure));
            assertTrue(body.contains(testLastCommitTime));
            assertTrue(body.contains(testFileContents));
        });
    }
}
