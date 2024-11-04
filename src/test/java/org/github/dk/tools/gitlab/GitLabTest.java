package org.github.dk.tools.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author D.K
 * @version V1.0.0
 * @since 1.8
 */
class GitLabTest {

    @Test
    void listOwnedProjects() throws GitLabApiException {
        GitLab gitLab = new GitLab.Builder()
                .setApiVersion(GitLabApi.ApiVersion.V4)
                .setHostUrl("https://gitlab.com/")
                .setPersonalAccessToken("")
                .build();

        List<Project> projectList = gitLab.listOwnedProjects();
        assertNotNull(projectList);
    }

    @Test
    void createBranch() throws GitLabApiException {
        GitLab gitLab = new GitLab.Builder()
                .setApiVersion(GitLabApi.ApiVersion.V4)
                .setHostUrl("https://gitlab.com/")
                .setPersonalAccessToken("")
                .build();
        String sourceBranch = "main";
        String targetBranch = "master930";

        List<Project> projectList = gitLab.listOwnedProjects();
        Project project = projectList.get(0);
        String createBranch = gitLab.createBranch(project, targetBranch, sourceBranch);
        assertEquals(targetBranch, createBranch);
    }

}
