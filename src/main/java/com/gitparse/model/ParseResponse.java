package com.gitparse.model;

public class ParseResponse {
    private String repoStructure;
    private String fileContents;
    private String lastCommitTime;

    public String getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(String lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }

    public String getRepoStructure() {
        return repoStructure;
    }

    public void setRepoStructure(String repoStructure) {
        this.repoStructure = repoStructure;
    }

    public String getFileContents() {
        return fileContents;
    }

    public void setFileContents(String fileContents) {
        this.fileContents = fileContents;
    }
}
