package org.github.dk.tools.gitlab.command;

import org.junit.jupiter.api.Test;
import picocli.CommandLine;

/**
 * @author D.K
 * @version V1.0.0
 * @since Java 17
 */
public class ModifyFileCommandTest {

    final String projectId = "62418892";
    final String hostUrl = "https://gitlab.com/";
    final String personalAccessToken = "";
    final String filePath = "README.md";

    /**
     * 测试修改指定分支
     */
    @Test
    public void modifySpecifiedBranchFile(){
        ModifyFileCommand modifyFileCommand = new ModifyFileCommand();
        CommandLine cmd = new CommandLine(modifyFileCommand);
        final String branchName = "master930";
        final String newContent = "测试提交-内容｜352@#¥@# dfdf 哈哈";
        final String commitMessage = "测试ModifyFileCommand，修改指定分支";

        String[] args = {
                "-u", hostUrl,
                "-t", personalAccessToken,
                "-p", projectId,
                "-b", branchName,
                "-f", filePath,
                "-c", newContent,
                "-m", commitMessage
        };
        cmd.execute(args);
    }

    /**
     * 测试修改所有分支
     */
    @Test
    public void modifySpecifiedAllBranchFile(){
        ModifyFileCommand modifyFileCommand = new ModifyFileCommand();
        CommandLine cmd = new CommandLine(modifyFileCommand);
        final String branchName = "master930";
        final String newContent = "测试提交-内容｜352@#¥@# dfdf 修复所有分支内容";
        final String commitMessage = "测试ModifyFileCommand，修改所有分支";

        String[] args = {
                "-u", hostUrl,
                "-t", personalAccessToken,
                "-p", projectId,
                "-f", filePath,
                "-c", newContent,
                "-m", commitMessage
        };
        cmd.execute(args);
    }

    /**
     * 测试正则替换
     */
    @Test
    public void modifyFileByRegex(){
        ModifyFileCommand modifyFileCommand = new ModifyFileCommand();
        CommandLine cmd = new CommandLine(modifyFileCommand);
        final String branchName = "master930";
        final String newContent = "--正则表达式替换数字--";
        final String commitMessage = "测试ModifyFileCommand，使用正则替换";
        final String regex = "\\d+";

        String[] args = {
                "-u", hostUrl,
                "-t", personalAccessToken,
                "-p", projectId,
                "-b", branchName,
                "-f", filePath,
                "-r", regex,
                "-c", newContent,
                "-m", commitMessage
        };
        cmd.execute(args);
    }
}
