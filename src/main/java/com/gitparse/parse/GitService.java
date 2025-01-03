package com.gitparse.parse;

import com.gitparse.model.ParseResponse;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class GitService {

    private static final Logger LOG = LoggerFactory.getLogger(GitService.class);

    public ParseResponse clone(String url) {
        ParseResponse response = new ParseResponse();
        try {
            var parts = url.split("/");
            var folder = Path.of("").toAbsolutePath() +"/"+parts[parts.length -1];
            var startTime = System.currentTimeMillis();
            LOG.info("Cloning Git Repository {}", url);
            //Perform a shallow clone
            var git = Git.cloneRepository().setDepth(1)
                    .setURI(url)
                    .setDirectory(new File(folder))
                    .call();

            // Get last commit
            var lastCommit = git.log().
                    setMaxCount(1).
                    call().
                    iterator().
                    next();

            LOG.info("Cloned Git Repository {} in {} ms ", url, System.currentTimeMillis()-startTime);
            StringBuilder fileAsText = new StringBuilder();
            response.setRepoStructure(ParseDirectory.readRepoStructure(folder,fileAsText));
            response.setFileContents(fileAsText.toString());

            Timestamp ts = new Timestamp(lastCommit.getCommitTime()* 1000L);
            Date date = new Date(ts.getTime());

            response.setLastCommitTime(date.toString());
        } catch (Exception e) {
            LOG.error("Issue Cloning Git Repository {}, {} ", url, e.getMessage());
            throw new RuntimeException(e);
        }
        return response;

    }

}

