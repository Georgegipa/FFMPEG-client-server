package Generic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProcessRunner {
    private final ProcessBuilder processBuilder;
    private Process process;

    public ProcessRunner(List<String> comm) {
        List<String> commands = new ArrayList<>();
        commands.add("cmd.exe");
        commands.add("/c");
        commands.addAll(comm);
        processBuilder = new ProcessBuilder(commands);
        //print all the commands
        System.out.print("\ncommands: ");
        for (String s : commands) {
            System.out.print(s + " ");
        }
        System.out.println("");
    }

    public void start() {
        try {
            process = processBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        process.destroy();
    }

    public void printError() {
        try {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String error;
            //print the process output
            while ((error = errorReader.readLine()) != null) {
                System.out.println(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
