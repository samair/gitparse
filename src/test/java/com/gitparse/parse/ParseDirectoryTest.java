package com.gitparse.parse;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ParseDirectoryTest {

    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("testDir");
    }

    @AfterEach
    void tearDown() throws IOException {
        ParseDirectory.deleteDirectory(tempDir.toFile());
    }

    @Test
    void testReadRepoStructure() throws IOException {
        // Arrange
        Path file1 = Files.createFile(tempDir.resolve("file1.txt"));
        Path file2 = Files.createFile(tempDir.resolve("file2.txt"));
        Path subDir = Files.createDirectory(tempDir.resolve("subDir"));
        Path file3 = Files.createFile(subDir.resolve("file3.txt"));
        StringBuilder fileAsText = new StringBuilder();

        // Act
        String structure = ParseDirectory.readRepoStructure(tempDir.toString(), fileAsText);

        // Assert
        assertTrue(structure.contains("file1.txt"));
        assertTrue(structure.contains("file2.txt"));
        assertTrue(structure.contains("subDir/"));
        assertTrue(structure.contains("file3.txt"));
        assertTrue(fileAsText.toString().contains("File Name - /file1.txt"));
        assertTrue(fileAsText.toString().contains("File Name - /subDir/file3.txt"));
        assertFalse(Files.exists(tempDir)); // Directory should be deleted
    }

    @Test
    void testPrintDirectoryTree() throws IOException {
        // Arrange
        Path subDir = Files.createDirectory(tempDir.resolve("subDir"));
        Files.createFile(subDir.resolve("file.txt"));
        StringBuilder fileAsText = new StringBuilder();

        // Act
        String structure = ParseDirectory.printDirectoryTree(tempDir.toFile(), tempDir.toString(), fileAsText);

        // Assert
        assertNotNull(structure);
        assertTrue(structure.contains("|--subDir/"));
        assertTrue(structure.contains("|  |--file.txt"));
    }

    @Test
    void testPrintFile() throws IOException {
        // Arrange
        Path file = Files.createFile(tempDir.resolve("file.txt"));
        Files.writeString(file, "Sample content");
        StringBuilder sb = new StringBuilder();
        StringBuilder fileAsText = new StringBuilder();

        // Act
        ParseDirectory.printFile(file.toFile(), 1, sb, tempDir.toString(), fileAsText);

        // Assert
        assertTrue(sb.toString().contains("|--file.txt"));
        assertTrue(fileAsText.toString().contains("File Name - /file.txt"));
        assertTrue(fileAsText.toString().contains("Sample content"));
    }

    @Test
    void testDeleteDirectory() throws IOException {
        // Arrange
        Path subDir = Files.createDirectory(tempDir.resolve("subDir"));
        Files.createFile(subDir.resolve("file.txt"));

        // Act
        ParseDirectory.deleteDirectory(tempDir.toFile());

        // Assert
        assertFalse(Files.exists(tempDir));
    }
}
