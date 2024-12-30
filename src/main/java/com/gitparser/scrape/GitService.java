package com.gitparser.scrape;

import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GitService {

    static StringBuilder folderName = new StringBuilder();
    static StringBuilder fileAsText = new StringBuilder();
    public List<String> clone(String url) {
        List<String> response = new ArrayList<>();
        try {
            var parts = url.split("/");
            var folder = Path.of("").toAbsolutePath() +"/"+parts[parts.length -1];
            var git = Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(new File(folder))
                    .call();
            var lastCommit = git.log().
                    setMaxCount(1).
                    call().
                    iterator().
                    next();
            response.add(readRepoStructure(folder));
            response.add(fileAsText.toString());
            System.out.println(lastCommit.getCommitTime());
            Timestamp ts = new Timestamp(lastCommit.getCommitTime()* 1000L);
            Date date = new Date(ts.getTime());
            response.add(date.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return response;

    }

    private String readRepoStructure(String folder) {
        File file = new File(folder);
        String structure = printDirectoryTree(file,folder);
        deleteDirectory(file);
        return structure;
    }

    public static String printDirectoryTree(File folder, String baseFolder) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        printDirectoryTree(folder, indent, sb, baseFolder);
        return sb.toString();
    }

    private static void printDirectoryTree(File folder, int indent,
                                           StringBuilder sb, String baseFolder) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        sb.append(getIndentString(indent));
        sb.append("+--");
        sb.append(folder.getName());
        sb.append("/");
        sb.append("\n");
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                printDirectoryTree(file, indent + 1, sb, baseFolder);
            } else {
                printFile(file, indent + 1, sb, baseFolder);
            }
        }

    }

    private static void printFile(File file, int indent, StringBuilder sb, String baseFolder) {
        sb.append(getIndentString(indent));
        sb.append("+--");
        sb.append(file.getName());
        sb.append("\n");

        try {
            var header =
                "=====================================================================\n"+
                "           File Name - %s                                            \n"+
                "=====================================================================";

            var content = Files.readString(file.toPath());
            var fileRelative = file.getCanonicalPath().replace(baseFolder,"");
            header = String.format(header, fileRelative);
            System.out.println(fileRelative);
            fileAsText.append(header);
            fileAsText.append(content);
        } catch (IOException e) {
            System.out.println("Error"+ e.getMessage());
        }
    }

    private static String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("|  ");
        }
        return sb.toString();
    }

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}

