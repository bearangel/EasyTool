package org.github.dk.tools.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.RepositoryFile;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author D.K
 * @version V1.0.0
 * @since 1.8
 */
class GitLabTest {

    GitLab getGitLab(){

        final String hostUrl = "https://gitlab.com/";
        final String personalAccessToken = "";

        return new GitLab.Builder()
                .setApiVersion(GitLabApi.ApiVersion.V4)
                .setHostUrl(hostUrl)
                .setPersonalAccessToken(personalAccessToken)
                .build();
    }

    /**
     * 测试获取我拥有的项目
     * @throws GitLabApiException
     */
    @Test
    void listOwnedProjects() throws GitLabApiException {
        GitLab gitLab = getGitLab();
        List<Project> projectList = gitLab.listOwnedProjects();
        assertNotNull(projectList);
    }

    /**
     * 测试创建分支
     * @throws GitLabApiException
     */
    @Test
    void createBranch() throws GitLabApiException {
        GitLab gitLab = getGitLab();
        String sourceBranch = "main";
        String targetBranch = "master" + new Random().nextInt(10000);

        List<Project> projectList = gitLab.listOwnedProjects();
        Project project = projectList.get(0);
        String createBranch = gitLab.createBranch(project, targetBranch, sourceBranch);
        assertEquals(targetBranch, createBranch);
    }

    @Test
    void getBranches() throws GitLabApiException {
        GitLab gitLab = getGitLab();
        long projectId = 62418892;
        Project project = gitLab.getProject(projectId);
        List<Branch> branches = gitLab.getBranches(project);
        assertNotNull(branches);
    }

    @Test
    void getRepositoryFile() throws GitLabApiException {
        GitLab gitLab = getGitLab();
        long projectId = 62418892;
        Project project = gitLab.getProject(projectId);
        Branch branche = gitLab.getBranches(project, "main");
        if (branche == null) {
            System.out.println("找不到分支");
        }
//        RepositoryFile repositoryFile = gitLab.getRepositoryFile(project, branche, )
    }

}
