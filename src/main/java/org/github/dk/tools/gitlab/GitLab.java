package org.github.dk.tools.gitlab;

import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Project;

import java.util.List;

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
