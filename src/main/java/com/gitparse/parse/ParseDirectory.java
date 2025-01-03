package com.gitparse.parse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface ParseDirectory {

    Logger LOG = LoggerFactory.getLogger(ParseDirectory.class);

    static String readRepoStructure(String folder, StringBuilder fileAsText) {
        File file = new File(folder);
        String structure = printDirectoryTree(file, folder, fileAsText);
        deleteDirectory(file);
        return structure;
    }

    static String printDirectoryTree(File folder, String baseFolder, StringBuilder fileAsText) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        printDirectoryTree(folder, indent, sb, baseFolder, fileAsText);
        return sb.toString();
    }

    static void printDirectoryTree(File folder, int indent,
                                   StringBuilder sb, String baseFolder, StringBuilder fileAsText) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        sb.append(getIndentString(indent));
        sb.append("|--");
        sb.append(folder.getName());
        sb.append("/");
        sb.append("\n");
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                printDirectoryTree(file, indent + 1, sb, baseFolder, fileAsText);
            } else {
                printFile(file, indent + 1, sb, baseFolder, fileAsText);
            }
        }

    }

    static void printFile(File file, int indent, StringBuilder sb, String baseFolder, StringBuilder fileAsText) {
        sb.append(getIndentString(indent));
        sb.append("|--");
        sb.append(file.getName());
        sb.append("\n");

        try {
            var header = """
                                     
                    ---------------------------------------------------------------------
                              File Name - %s
                    ---------------------------------------------------------------------
                                     
                    """;

            var content = Files.readString(file.toPath());
            var fileRelative = file.getAbsolutePath().replace(baseFolder, "");
            header = String.format(header, fileRelative);

            LOG.debug(fileRelative);

            fileAsText.append(header);
            fileAsText.append(content);
        } catch (IOException e) {
            LOG.error("Error {}", e.getMessage());
        }
    }

    static String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("|  ");
        }
        return sb.toString();
    }

    static void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}
