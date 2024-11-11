package org.github.dk.tools;

import org.github.dk.tools.gitlab.command.GitLabCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.Spec;
import java.util.Locale;

@Command(name = "Main", subcommands = { GitLabCommand.class, CommandLine.HelpCommand.class }, // |2|
        description = "Resolve ISO country codes (ISO-3166-1) or language codes (ISO 639-1 or -2)",mixinStandardHelpOptions = true)
public class Main implements Runnable { // |1|
    @Spec CommandSpec spec;

    @Command(name = "country", description = "Resolve ISO country code (ISO-3166-1, Alpha-2 code)") // |3|
    void subCommandViaMethod(@Parameters(arity = "1..*", paramLabel = "<country code>",
            description = "country code(s) to be resolved") String[] countryCodes) {
        for (String code : countryCodes) {
            System.out.println(String.format("%s: %s", code.toUpperCase(), new Locale("", code).getDisplayCountry()));
        }
    }

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Specify a subcommand");
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}

