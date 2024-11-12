package org.github.dk.tools;

import org.github.dk.tools.gitlab.command.GitLabCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;


@Command(name = "Main",
        subcommands = { GitLabCommand.class, CommandLine.HelpCommand.class },
        mixinStandardHelpOptions = true)
public class Main implements Runnable {

    @Override
    public void run() {
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}

