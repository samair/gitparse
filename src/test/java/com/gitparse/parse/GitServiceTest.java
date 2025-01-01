package com.gitparse.parse;

import com.gitparse.model.ParseResponse;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GitServiceTest {

    private GitService gitService;
    private Git mockGit;
    private LogCommand mockLogCommand;
    private RevCommit mockRevCommit;

    @BeforeEach
    void setUp() {
        gitService = spy(new GitService());
        mockGit = mock(Git.class);
        mockLogCommand = mock(LogCommand.class);
        mockRevCommit = mock(RevCommit.class);
    }

    @Test
    void testCloneSuccess() throws Exception {
        // Mock URL and folder structure
        String repoUrl = "https://github.com/user/repo";
        String folder = Path.of("").toAbsolutePath() + "/repo";

        // Mock behavior for Git clone
        when(mockGit.log()).thenReturn(mockLogCommand);
        when(mockLogCommand.setMaxCount(1)).thenReturn(mockLogCommand);
        when(mockLogCommand.call()).thenReturn(Collections.singletonList(mockRevCommit));
        when(mockRevCommit.getCommitTime()).thenReturn((int) (new Date().getTime() / 1000));

        CloneCommand cloneCommand = mock(CloneCommand.class);
        Mockito.mockStatic(Git.class);
        when(Git.cloneRepository()).thenReturn(cloneCommand);
        when(cloneCommand.setDepth(1)).thenReturn(cloneCommand);
        when(cloneCommand.setURI(repoUrl)).thenReturn(cloneCommand);
        when(cloneCommand.setDirectory(any())).thenReturn(cloneCommand);
        when(cloneCommand.call()).thenReturn(mockGit);
        doReturn("Mocked repo structure").when(gitService).readRepoStructure(folder);

        // Call the method
        ParseResponse response = gitService.clone(repoUrl);

        // Verify interactions
        verify(mockGit, times(1)).log();
        verify(mockLogCommand, times(1)).setMaxCount(1);
        verify(mockLogCommand, times(1)).call();

        // Assert response
        assertNotNull(response);
        assertNotNull(response.getRepoStructure());
        assertNotNull(response.getFileContents());
        assertNotNull(response.getLastCommitTime());
    }

    @Test
    void testCloneFailure() throws GitAPIException {
        // Mock URL
        String repoUrl = "https://invalid-url/repo";

        // Mock behavior to throw an exception
        CloneCommand cloneCommand = mock(CloneCommand.class);

        when(Git.cloneRepository()).thenReturn(cloneCommand);
        when(cloneCommand.setDepth(1)).thenReturn(cloneCommand);
        when(cloneCommand.setURI(repoUrl)).thenReturn(cloneCommand);
        when(cloneCommand.setDirectory(any(File.class))).thenReturn(cloneCommand);
        when(cloneCommand.call()).thenThrow(new RuntimeException("Clone failed"));

        // Assert exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> gitService.clone(repoUrl));
        assertEquals("java.lang.RuntimeException: Clone failed", exception.getMessage());
    }
}
