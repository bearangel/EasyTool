package org.github.dk.tools.gitlab.command;

import picocli.CommandLine;

/**
 * GitLab命令行工具
 *
 * @author D.K
 * @version V1.0.0
 * @since Java 17
 */

@CommandLine.Command(name = "gitLab", version = "1.0.0",
        mixinStandardHelpOptions = true, subcommands = {ModifyFileCommand.class})
public class GitLabCommand implements Runnable{

    @Override
    public void run() {

    }
}
