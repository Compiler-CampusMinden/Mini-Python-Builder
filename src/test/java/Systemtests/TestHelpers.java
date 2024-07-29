/* (C)2024 */
package Systemtests;

import java.io.*;
import java.nio.file.Path;

public class TestHelpers {

    public static String getProgramOutput(Path workDirectory)
            throws IOException, InterruptedException {
        String line;
        StringBuilder result = new StringBuilder();
        ProcessBuilder runner = new ProcessBuilder("./bin/program");
        runner.directory(workDirectory.toFile());
        // runner.inheritIO(); //You can not redirect the stdout/stderr because the Reader will not

        Process rp = runner.start();

        InputStream processStdOutput = rp.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);

        while ((line = br.readLine()) != null) {
            result.append(line).append("\n");
        }

        // String result = new String(rp.getInputStream().readAllBytes());

        if (rp.waitFor() != 0) {
            throw new RuntimeException("Program returned with value != 0");
        }
        return result.toString();
    }

    public static String getProgramOutputBasedOnInput(
            Path workDirectory, String result, String input)
            throws IOException, InterruptedException {
        String line;
        ProcessBuilder runner = new ProcessBuilder("./bin/program");
        runner.directory(workDirectory.toFile());
        // runner.inheritIO(); //You can not redirect the stdout/stderr because the Reader will not
        // be able to get the output. :)

        Process rp = runner.start();

        OutputStream os = rp.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        pw.write(input);
        pw.flush();

        InputStream processStdOutput = rp.getInputStream();
        Reader r = new InputStreamReader(processStdOutput);
        BufferedReader br = new BufferedReader(r);

        StringBuilder resultBuilder = new StringBuilder(result);
        while ((line = br.readLine()) != null) {
            resultBuilder.append(line).append("\n");
        }
        result = resultBuilder.toString();

        // String result = new String(rp.getInputStream().readAllBytes());

        if (rp.waitFor() != 0) {
            throw new RuntimeException("Program returned with value != 0");
        }
        return result;
    }

    public static void makeProgram(Path workDirectory) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("make");
        builder.directory(workDirectory.toFile());
        // builder.inheritIO();

        Process p = builder.start();

        if (p.waitFor() != 0) {
            System.out.println("Could not make program !");
            throw new RuntimeException("Could not make program !");
        }
    }
}
