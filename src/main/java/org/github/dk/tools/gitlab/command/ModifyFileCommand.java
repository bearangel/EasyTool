package org.github.dk.tools.gitlab.command;

import org.github.dk.tools.gitlab.GitLab;
import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.RepositoryFile;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

/**
 * 修改文件
 * <p>
 * gitlab modifyFile -u https://www.gitlab.com -t token -b branchName -p projectId -f filePath -r regex -c content -m commitMessage
 *
 * @author D.K
 * @version V1.0.0
 * @since Java 17
 */
@Command(name = "modifyFile", mixinStandardHelpOptions = true, version = "modifyFile 1.0",
        description = "修改gitlab中的文件，并提交")
public class ModifyFileCommand implements Runnable {

    @Option(names = {"-u", "--url"}, required = true, description = "GitLab地址,如：https://gitlab.com")
    private String gitLabUrl;

    @Option(names = {"-t", "--token"}, required = true, description = "访问令牌")
    private String accessToken;

    @Option(names = {"--apiVersion"}, description = "gitlab api版本，可选择v3、v4，默认值是v4", defaultValue = "v4")
    private String apiVersion;

    @Option(names = {"-p", "--projectId"}, required = true, description = "项目ID")
    private Long projectId;

    @Option(names = {"-b", "--branch"}, description = "指定操作分支名称，如没有指定参数则修改所有分支")
    private String branchName;

    @Option(names = {"-f", "--filePath"}, required = true, description = "待修改git文件路径，如：src/main/java/main.java")
    private String filePath;

    @Option(names = {"-r", "--regex"}, description = "正则表达式匹配内容")
    private String regex;

    @Option(names = {"-c", "--content"}, required = true, description = "替换的内容，如果-r参数存在，则将这个参数内容做正则替换")
    private String newContent;

    @Option(names = {"-m", "--message"}, required = true, description = "提交信息")
    private String commitMessage;

    @Override
    public void run() {
        try {
            GitLabApi.ApiVersion gitLabApiVersion = switch (apiVersion) {
                case "v3" -> GitLabApi.ApiVersion.V3;
                default -> GitLabApi.ApiVersion.V4;
            };
            System.out.println(MessageFormat.format("开始使用{0}版本API操作gitLab", apiVersion));
            GitLab gitLab = new GitLab.Builder()
                    .setApiVersion(gitLabApiVersion)
                    .setHostUrl(gitLabUrl)
                    .setPersonalAccessToken(accessToken)
                    .build();

            List<String> branchNameList = getBranches(gitLab);
            branchNameList.forEach(tempBranchName -> {
                try {
                    System.out.println(MessageFormat.format("开始修改项目{0}-分支{1}-文件{2}", projectId, tempBranchName, filePath));
                    RepositoryFile repositoryFile = gitLab.getRepositoryFile(projectId, tempBranchName, filePath);
                    if (repositoryFile == null) {
                        System.out.println(MessageFormat.format("无法获取项目{0}中{1}分支的{2}文件", projectId, tempBranchName, filePath));
                        return;
                    }

                    String commitContent = "";
                    if (regex != null) {
                        String oldContent = getDecodedContentAsString(repositoryFile);
                        commitContent = oldContent.replaceAll(regex, newContent);
                    } else {
                        commitContent = newContent;
                    }

                    repositoryFile.setContent(Base64.getEncoder().encodeToString(commitContent.getBytes(StandardCharsets.UTF_8)));
                    gitLab.updateFile(projectId, repositoryFile, tempBranchName, commitMessage);
                    System.out.println(MessageFormat.format("修改项目{0}-分支{1}-文件{2}完成，提交信息：{3}", projectId, tempBranchName, filePath, commitMessage));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分支
     * @param gitLab
     * @return
     * @throws GitLabApiException
     */
    private List<String> getBranches(GitLab gitLab) throws GitLabApiException {
        if(branchName != null && !branchName.isEmpty()) {
            return Collections.singletonList(branchName);
        }
        return gitLab.getBranches(projectId).stream().map(Branch::getName).toList();
    }

    /**
     * 由于返回的base64字符串可能会出现\n\r,默认的{@link RepositoryFile#getDecodedContentAsString()}会出现错误，所以这里自行处理
     * @param repositoryFile 文件对象
     * @return 返回解码字符串
     */
    private String getDecodedContentAsString(RepositoryFile repositoryFile) {
        String content = repositoryFile.getContent();
        if (Constants.Encoding.BASE64.equals(repositoryFile.getEncoding())) {
            content = content.replace("\n", "").replace("\r", "");
            return new String(Base64.getDecoder().decode(content));
        }
        return content;
    }
}
