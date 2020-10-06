import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SemantTest {
    @Test
    public void testExample() {
        String programFilename = "tests/good.cl";

        runSemant(programFilename);
        checkAgainstRef(programFilename);
    }

    private void runSemant(String programFilename) {
        InputStream stdIn = System.in;
        PrintStream stdOut = System.out;

        String cmd = "../PA2J/lexer " + programFilename + " | ../PA3J/parser " + programFilename;

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("/bin/sh", "-c", cmd);

        try {
            Process process = processBuilder.start();

            System.setIn(process.getInputStream());
            System.setOut(new PrintStream(new FileOutputStream(programFilename + "_test.txt")));

            Semant.main(new String[] { programFilename });

            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.setIn(stdIn);
            System.setOut(stdOut);
        }
    }

    private void checkAgainstRef(String programFilename) {
        try {
            List<String> file1 = Files.readAllLines(Paths.get(programFilename + "_ref.txt"));
            List<String> file2 = Files.readAllLines(Paths.get(programFilename + "_test.txt"));

            assertEquals(file1.size(), file2.size());

            for (int i = 0; i < file1.size(); i++) {
                String refString = file1.get(i);
                if (refString.trim().startsWith("#")) {
                    // skip line numbers due to bug in ref parser
                    continue;
                }
                String testString = file2.get(i);
                assertEquals(refString, testString, "Failed at line " + (i+1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}