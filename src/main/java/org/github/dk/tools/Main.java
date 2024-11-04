package org.github.dk.tools;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * 启动入口
 *
 * @author D.K
 * @version V1.0.0
 * @since 1.8
 */
public class Main {

    public static void main(String[] args) {
        try {
            // 创建终端和 LineReader 对象
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).completer(new StringsCompleter("exit")).build();

            String line;
            while ((line = lineReader.readLine("> ")) != null) {
                if ("exit".equals(line)) {
                    break;
                }
                System.out.println("You entered: " + line);
            }

            terminal.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
