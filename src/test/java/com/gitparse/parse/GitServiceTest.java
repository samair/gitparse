package com.gitparse.parse;

import com.gitparse.model.ParseResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class GitServiceTest {

    private static final String TEST_REPO_URL = "https://github.com/samair/gitparse"; // Replace with a real test repo URL
    private Path clonedRepoPath;

    @AfterEach
    void cleanUp() {
        if (clonedRepoPath != null && clonedRepoPath.toFile().exists()) {
            ParseDirectory.deleteDirectory(clonedRepoPath.toFile());
        }
    }

    @Test
    void testCloneValidRepository() {
        // Arrange
        GitService gitService = new GitService();

        // Act
        ParseResponse response = gitService.clone(TEST_REPO_URL);
        clonedRepoPath = Path.of("", TEST_REPO_URL.split("/")[TEST_REPO_URL.split("/").length - 1]);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getRepoStructure());
        assertNotNull(response.getFileContents());
        assertNotNull(response.getLastCommitTime());

        assertTrue(response.getRepoStructure().contains("|--")); // Validate structure format
    }

    @Test
    void testCloneInvalidRepositoryUrl() {
        // Arrange
        GitService gitService = new GitService();
        String invalidRepoUrl = "https://github.com/samair/gitparsessss";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> gitService.clone(invalidRepoUrl));
    }
}
