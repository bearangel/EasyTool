package org.github.dk.tools.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.RepositoryFile;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author D.K
 * @version V1.0.0
 * @since 1.8
 */
class GitLabTest {

    final long projectId = 62418892;

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
        String createBranch = gitLab.createBranch(project, sourceBranch, targetBranch);
        assertEquals(targetBranch, createBranch);
    }

    /**
     * 测试获取项目分支
     * @throws GitLabApiException
     */
    @Test
    void getBranches() throws GitLabApiException {
        GitLab gitLab = getGitLab();
        List<Branch> branches = gitLab.getBranches(projectId);
        assertNotNull(branches);
    }

    /**
     * 测试获取分支文件内容
     * @throws GitLabApiException
     */
    @Test
    void getRepositoryFile() throws GitLabApiException {
        GitLab gitLab = getGitLab();
        Project project = gitLab.getProject(projectId);
        Branch branch = gitLab.getBranch(project, "master930");
        if (branch == null) {
            System.out.println("找不到分支");
            return;
        }
        String filePath = "README.md";
        RepositoryFile repositoryFile = gitLab.getRepositoryFile(project, branch, filePath);
        assertNotNull(repositoryFile.getContent());
    }

    /**
     * 测试文件修改
     * @throws GitLabApiException
     */
    @Test
    void updateFile() throws GitLabApiException {
        GitLab gitLab = getGitLab();
        String branchName = "master930";
        String filePath = "README.md";
        RepositoryFile repositoryFile = gitLab.getRepositoryFile(projectId, branchName, filePath);
        String newContent = new String(Base64.getEncoder()
                .encodeToString("test测试!@#$%^&*()_+|｜」「：“……&#¥%……&#¥%@#……#¥%".getBytes(StandardCharsets.UTF_8)));
        repositoryFile.setContent(newContent);
        gitLab.updateFile(projectId, repositoryFile, branchName, "测试提交");
    }


}
