package CBuilder;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;
import com.pholser.junit.quickcheck.From;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import static org.junit.Assert.*;

@RunWith(JQF.class)
public class CompilerFuzzTest {
    // Base Path for Fuzzer to write Programs to.
    private static String BASE_PATH = "./build/compilerOutput/Fuzzing/";

    private void compile(ProgramBuilder code) {
        File folder = new File(BASE_PATH + code.hashCode());
        //Creating Directory was successful
        assertTrue(String.valueOf(code.hashCode()),folder.exists());
        File[] listOfFiles = folder.listFiles();
        List<String> dirs = new ArrayList<>();
        List<String> files = new ArrayList<>();
        //Ordner hat Inhalt
        assertTrue(listOfFiles.length > 0);
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                files.add(listOfFile.getName());
            } else if (listOfFile.isDirectory()) {
                dirs.add(listOfFile.getName());
            }
        }
        //Copying Template was successful
        assertTrue(String.valueOf(code.hashCode()),files.contains("Makefile"));
        assertTrue(String.valueOf(code.hashCode()),files.contains(".gitignore"));
        assertTrue(String.valueOf(code.hashCode()),files.contains("Doxyfile"));
        assertTrue(String.valueOf(code.hashCode()),dirs.contains("test"));
        assertTrue(String.valueOf(code.hashCode()),dirs.contains("include"));
        assertTrue(String.valueOf(code.hashCode()),dirs.contains("src"));
        if (System.getProperty("compile") != null) {
            int exitcode = -1;
            try {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(folder);
                processBuilder.command("make");


                Process process = processBuilder.start();
                exitcode = process.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //Compile Process was successful
            assertEquals(String.valueOf(code.hashCode()), 0, exitcode);
            List<String> lines = new ArrayList<String>();
            try {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(folder);
                processBuilder.command("./bin/program");
                Process process = processBuilder.start();
                exitcode = process.waitFor();
                BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertFalse(String.valueOf(code.hashCode()), lines.isEmpty());
            assertEquals(String.valueOf(code.hashCode()), 0, exitcode);
            assertTrue(String.valueOf(code.hashCode()), lines.get(0).equals("----------------FUZZING-Start"));
            assertTrue(String.valueOf(code.hashCode()), lines.get(lines.size() - 1).equals("----------------FUZZING-End"));
        }
    }

    @Fuzz
    public void testWithGenerator(@From(CBuilderGenerator.class) ProgramBuilder code)  {
        compile(code);
    }
}
