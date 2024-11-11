package org.github.dk.tools.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.RepositoryFile;
import org.gitlab4j.api.models.RepositoryFileResponse;

import java.util.List;
import java.util.Objects;

/**
 * GitLab工具类
 *
 * @author D.K
 * @version V1.0.0
 * @since 1.8
 */
public class GitLab {

    private GitLabApi gitLabApi;

    private GitLab(String hostUrl, String personalAccessToken) {
        this.gitLabApi = new GitLabApi(hostUrl, personalAccessToken);
    }

    private GitLab(GitLabApi.ApiVersion apiVersion, String hostUrl, String personalAccessToken) {
        this.gitLabApi = new GitLabApi(apiVersion, hostUrl, personalAccessToken);
    }

    /**
     * 获取所有属于自己的项目
     *
     * @return
     * @throws GitLabApiException
     */
    public List<Project> listOwnedProjects() throws GitLabApiException {
        return gitLabApi.getProjectApi().getOwnedProjects();
    }

    /**
     * 通过项目ID获取项目对象
     *
     * @param projectId 项目ID
     * @return 方法会项目对象 {@link Project}
     * @throws GitLabApiException
     */
    public Project getProject(long projectId) throws GitLabApiException {
        return gitLabApi.getProjectApi().getProject(projectId);
    }

    /**
     * 创建分支
     *
     * @param project      项目对象
     * @param sourceBranch 源分支名称
     * @param targetBranch 新分支名称
     * @return 返回新创建分支名称
     * @throws GitLabApiException
     */
    public String createBranch(Project project, String sourceBranch, String targetBranch) throws GitLabApiException {
        Branch branch = gitLabApi.getRepositoryApi().createBranch(project.getId(), targetBranch, sourceBranch);
        return branch.getName();
    }

    /**
     * 获取某个项目所有分支
     *
     * @param project 项目对象 {@link Project}
     * @return 返回该项目分支列表 {@link Branch}
     * @throws GitLabApiException
     */
    public List<Branch> getBranches(Project project) throws GitLabApiException {
        long projectId = project.getId();
        return gitLabApi.getRepositoryApi().getBranches(projectId);
    }

    /**
     * 获取分支
     *
     * @param project    项目对象
     * @param branchName 分支名称
     * @return 获取分支对象，如果没有则返回null
     * @throws GitLabApiException
     */
    public Branch getBranch(Project project, String branchName) throws GitLabApiException {
        long projectId = project.getId();
        List<Branch> branches = gitLabApi.getRepositoryApi().getBranches(projectId, branchName);
        return branches.stream()
                .filter(branch -> Objects.equals(branch.getName(), branchName))
                .findFirst().orElse(null);
    }

    /**
     * 获取项目中，某个分支下的文件
     *
     * @param project  项目名对象
     * @param branch   分支对象
     * @param filePath 文件路径
     * @return
     * @throws GitLabApiException
     */
    public RepositoryFile getRepositoryFile(final Project project, final Branch branch, final String filePath) throws GitLabApiException {
        long projectId = project.getId();
        String branchName = branch.getName();
        return gitLabApi.getRepositoryFileApi().getFile(projectId, filePath, branchName);
    }

    /**
     * 更新文件
     *
     * @param project       项目对象
     * @param file          文件对象
     * @param branchName    分支名称
     * @param commitMessage 提交信息
     * @throws GitLabApiException 异常
     */
    public RepositoryFileResponse updateFile(final Project project, final RepositoryFile file, final String branchName, final String commitMessage) throws GitLabApiException {
        long projectId = project.getId();
        return gitLabApi.getRepositoryFileApi().updateFile(projectId, file, branchName, commitMessage);
    }

    /**
     * Gitlab创建对象，当apiVersion为null时候，gitlab默认使用v4 api
     */
    public static class Builder {

        private GitLabApi.ApiVersion apiVersion;

        private String hostUrl;

        private String personalAccessToken;

        public Builder setApiVersion(GitLabApi.ApiVersion apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public Builder setHostUrl(String hostUrl) {
            this.hostUrl = hostUrl;
            return this;
        }

        public Builder setPersonalAccessToken(String personalAccessToken) {
            this.personalAccessToken = personalAccessToken;
            return this;
        }

        public GitLab build() {
            if (apiVersion != null) {
                return new GitLab(this.apiVersion, this.hostUrl, this.personalAccessToken);
            }
            return new GitLab(this.hostUrl, this.personalAccessToken);
        }
    }

}
